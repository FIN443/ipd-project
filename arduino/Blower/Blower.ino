#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <SoftwareSerial.h>
#include<Servo.h>
#define Servo_Moter 3
#define fan_turbo 4
#define fan_silent 5
#define mode_switch 6
#define BT_TXD 7
#define BT_RXD 8
#define mode_relay_1 9
#define mode_relay_2 10
//#define btn_turbo 10
//#define btn_off 11

Servo SV;
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
LiquidCrystal_I2C lcd(0x27,20,4);    // LCD패널

char ppm;
int fan = 2;
float RS_gas = 0;
float ratio = 0;
float sensorValue = 0;
float sensor_volt = 0;
float R0 = 7200.0;
unsigned long previous_switch, previous_fan, previous_time, current_time;
 
void setup() {
  pinMode(fan_turbo, OUTPUT);
  pinMode(fan_silent, OUTPUT);
  pinMode(mode_relay_1, OUTPUT);
  pinMode(mode_relay_2, OUTPUT);
  pinMode(mode_switch, INPUT);
//  pinMode(btn_off, INPUT);
 
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0,0);
  Serial.begin(9600);
  bluetooth.begin(9600);
  previous_time = millis();
  previous_fan = millis();
  SV.attach(Servo_Moter); // 모터 3번핀
  SV.write(90);
  digitalWrite(fan_turbo, HIGH);
  digitalWrite(fan_silent, HIGH);
  digitalWrite(mode_relay_1, HIGH);
  digitalWrite(mode_relay_2, HIGH);
}

//void None(){
//  if (Serial.available()) {
//    lcd.clear();
//    while (Serial.available() > 0) {
//      lcd.setCursor(0,0);
//      lcd.write(Serial.print("PPM: "));
//      lcd.write(Serial.println(ppm));
//      lcd.setCursor(0,1);  
//      lcd.write(Serial.print("None CO Gas"));
//    }
//  }
//}
//
//void Safe(){
//  if (Serial.available()) {
//    lcd.clear();
//    while (Serial.available() > 0) {
//      lcd.setCursor(0,0);
//      lcd.write(Serial.print("PPM: "));
//      lcd.write(Serial.println(ppm));
//      lcd.setCursor(0,1);
//      lcd.write(Serial.print("Safe"));
//    }
//  }  
//}
//
//void Warnning(){
//  if (Serial.available()) {
//    lcd.clear();
//    while (Serial.available() > 0) {
//      lcd.setCursor(0,0);
//      lcd.write(Serial.print("PPM: "));
//      lcd.write(Serial.println(ppm));  
//      lcd.setCursor(0,1);  
//      lcd.write(Serial.print("Warnning"));
//    }
//  }  
//}
//
//void Danger(){
//  if (Serial.available()) {
//    lcd.clear();
//    while (Serial.available() > 0) {
//      lcd.setCursor(0,0);
//      lcd.write(Serial.print("PPM: "));
//      lcd.write(Serial.println(ppm));   
//      lcd.setCursor(0,1); 
//      lcd.write(Serial.print("Danger"));
//    }
//  }  
//}

void loop() {
  sensorValue = analogRead(A0);
  sensor_volt = sensorValue/1024*5.0;
  RS_gas = (5.0-sensor_volt)/sensor_volt;
  ratio = RS_gas/R0;
  float x = 1538.46 * ratio;
  float ppm = pow(x,-1.709);
  int temp = (int)(ppm*100);
  current_time = millis();
  if(current_time-previous_time>1000){ // 블루투스 시간 간격
//    Serial.print("PPM: ");
//    Serial.print(ppm);
//    Serial.println();
    bluetooth.print(temp); // 블루투스 송신
    bluetooth.println();
    previous_time = current_time;
  }

  if(digitalRead(mode_switch)==HIGH) {
//    Serial.println("switch: ON");
    digitalWrite(mode_relay_1, LOW);
    digitalWrite(mode_relay_2, LOW);
    digitalWrite(fan_turbo, HIGH);
    digitalWrite(fan_silent, HIGH);
  }
  
  if(digitalRead(mode_switch)==LOW) {
//    Serial.println("switch: OFF");
    digitalWrite(mode_relay_1, HIGH);
    digitalWrite(mode_relay_2, HIGH);
  }

//  if(digitalRead(btn_silent)==LOW && digitalRead(btn_turbo)==LOW) { // 꺼짐 버튼
//    if(digitalRead(mode_switch)==LOW) { // 수동모드 일 때
//        digitalWrite(fan_turbo, HIGH);
//        digitalWrite(fan_silent, HIGH);
//      }
//  }
//  if(digitalRead(btn_silent)==HIGH) { // 1단계 버튼
//    Serial.println("pin9");
//    if(digitalRead(mode_switch)==LOW) { // 수동모드 일 때
//        digitalWrite(fan_turbo, HIGH);
//        digitalWrite(fan_silent, LOW);
//      }
//  }
//  if(digitalRead(btn_turbo)==HIGH) { // 2단계 버튼
//    Serial.println("pin10");
//    if(digitalRead(mode_switch)==LOW) { // 수동모드 일 때
//        digitalWrite(fan_turbo, LOW);
//        digitalWrite(fan_silent, HIGH);
//      }
//  }
//  if(current_time-previous_switch>1000){
//  
//    previous_switch = current_time;
//  }

  if(current_time-previous_fan>1000) { // FAN 1초 간격
    if(temp <= 50 && temp>0){ // Safe
//      digitalWrite(fan,HIGH);
//      analogWrite(fan,180);
//      Serial.print("PPM: ");
//      Serial.println("safe");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("PPM: ");
      lcd.print(ppm);   
      lcd.setCursor(0,1); 
      lcd.print("Safe");
      if(digitalRead(mode_switch)==LOW) { // 자동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Auto");
        digitalWrite(fan_turbo, HIGH);
        digitalWrite(fan_silent, HIGH);
      }
      else if(digitalRead(mode_switch)==HIGH) { // 수동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Manual");
      }
    }
    else if(temp >50 && temp <= 100){ // Warnning
//      digitalWrite(fan,HIGH);
//      analogWrite(fan,225);
//      Serial.print("PPM: ");
//      Serial.println("Warnning");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("PPM: ");
      lcd.print(ppm);   
      lcd.setCursor(0,1); 
      lcd.print("Warnning");
      if(digitalRead(mode_switch)==LOW) { // 자동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Auto");
        digitalWrite(fan_turbo, HIGH);
        digitalWrite(fan_silent, LOW);
      }
      else if(digitalRead(mode_switch)==HIGH) { // 수동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Manual");
      }
    }
    else if(temp >100){ // Danger
//      digitalWrite(fan,HIGH);
//      analogWrite(fan,255);
//      Serial.print("PPM: ");
//      Serial.println("Danger");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("PPM: ");
      lcd.print(ppm);   
      lcd.setCursor(0,1); 
      lcd.print("Danger");
      if(digitalRead(mode_switch)==LOW) { // 자동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Auto");
        digitalWrite(fan_turbo, LOW);
        digitalWrite(fan_silent, HIGH);
      }
      else if(digitalRead(mode_switch)==HIGH) { // 수동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Manual");
      }
    }
    else { // None
//      digitalWrite(fan,LOW);
//      Serial.print("PPM: ");
//      Serial.println("Npne");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("PPM: ");
      lcd.print(ppm);   
      lcd.setCursor(0,1); 
      lcd.print("None CO Gas");
      if(digitalRead(mode_switch)==LOW) { // 자동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Auto");
        digitalWrite(fan_turbo, HIGH);
        digitalWrite(fan_silent, HIGH);
      }
      else if(digitalRead(mode_switch)==HIGH) { // 수동모드 일 때
        lcd.setCursor(0,3);
        lcd.print("Mode: Manual");
      }
    }
    previous_fan = current_time;
  }
  
  if(bluetooth.available()) { // 외부로부터 송신이 있을 때
    Serial.write(bluetooth.read()); // 블루투스 수신
  }

//  if(digitalRead(6)==HIGH) {
//    Serial.write("SWITCH HIGH");
//  }
//  else if(digitalRead(6)==LOW) {
//    Serial.write("SWITCH LOW");
//  }
  
   delay(100); // 렉 방지 딜레이
}