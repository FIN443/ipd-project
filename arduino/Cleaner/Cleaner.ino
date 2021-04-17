#include <Wire.h> 
#include <OneWire.h>
#include <DallasTemperature.h>
#include <LiquidCrystal_I2C.h>
#define ONE_WIRE_BUS 2 // 수온센서
#define relay_valve_in 3 // 벨브in
#define relay_valve_out 4 // 벨브out
#define relay_heater 5 // 히터
#define relay_uv 6 // UV램프
#define relay_cleaner1 7 // 초음파1
#define relay_cleaner2 8 // 초음파2


//커뮤니케이션을 준비합니다.
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
LiquidCrystal_I2C lcd(0x27, 20,4);    // LCD패널

int Raindrops_pin = A0;  // 빗방울센서 핀을 A0으로 설정
int arduino_state; // 아두이노 제어
int boundary_state; // 수위높이 체크
int temp_ok; // 온도 적정 확인
int cleaner_state; // 초음파 사용 여부
int clean_done; // 세척 완료 여부
int clean_count; // 세척 횟수
int minute; // 분
int sec; // 초
int heat_time; // 가열 시간
int in_time; // 물 들어오는 시간
float water_temp;
long prev_time, prev_clean, prev_out, curr_time;

void setup(void)
{
  Serial.begin(9600);
  sensors.begin();
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0,0);
  pinMode(A0 , INPUT);   // 빗방울 감지핀을 아날로그 입력으로 설정
  pinMode(relay_valve_in, OUTPUT);
  pinMode(relay_valve_out, OUTPUT);
  pinMode(relay_heater, OUTPUT);
  pinMode(relay_cleaner1, OUTPUT);
  pinMode(relay_cleaner2, OUTPUT);
  pinMode(relay_uv, OUTPUT);
  arduino_state = 1;
  boundary_state = 0;
  cleaner_state = 0;
  clean_done = 0;
  clean_count = 0;
  temp_ok = 0;
  digitalWrite(relay_valve_in, HIGH);
  digitalWrite(relay_valve_out, HIGH);
  digitalWrite(relay_heater, HIGH);
  digitalWrite(relay_cleaner1, HIGH);
  digitalWrite(relay_cleaner2, HIGH);
  digitalWrite(relay_uv, LOW);
  prev_time = millis();
  heat_time = 0;
  in_time = 0;
  lcd.clear();  
  lcd.setCursor(0,0);
  lcd.print("Time: 40:00");
  lcd.setCursor(0,1);
  lcd.print("State: ");
  lcd.print("Start");
  delay(5000);
}

void test() {
  Serial.print(arduino_state);
  Serial.print(", ");
  Serial.print(boundary_state);
  Serial.print(", ");
  Serial.print(cleaner_state);
  Serial.print(", ");
  Serial.print(clean_done);
  Serial.print(", ");
  Serial.println(clean_count);
  Serial.print("temperature: ");
  Serial.println(water_temp);
}

void loop(void){ 
  curr_time = millis();
  minute = (120000-curr_time)/60000 + (heat_time/1000)/60; // 남은시간/1분
  sec = ((120000-curr_time)/1000) - minute*60 + (heat_time/1000);
  sensors.requestTemperatures(); 
  water_temp = sensors.getTempCByIndex(0);
//  test();

  lcd.clear();
  lcd.setCursor(0,2);
  lcd.print("Remaining: ");
  lcd.print(3-clean_count);
  if(minute < 0) {
    lcd.setCursor(0,0);
    lcd.print("Time: 00:00");
    if(arduino_state == 0) {
      lcd.setCursor(0,1);
      lcd.print("State: Finish");
      lcd.setCursor(0,2);
      lcd.print("Remaining: 0");
    }
    else {
      lcd.setCursor(0,1);
      lcd.print("State: Start");
    }
  }
  else {
    lcd.setCursor(0,0);
    lcd.print("Time: ");
    lcd.print(minute);
    lcd.setCursor(8,0);
    lcd.print(":");
    lcd.setCursor(9,0);
    lcd.print(sec);
    if(sec<10 && sec>=0) {
      lcd.setCursor(9,0);
      lcd.print("0");
      lcd.print(sec);
    }
    if(minute<10 && minute>=0) {
      lcd.setCursor(6,0);
      lcd.print("0");
      lcd.print(minute);
    }
    lcd.setCursor(0,1);
    lcd.print("State: ");
    lcd.print("Start");
  }
  
  if(arduino_state == 1) {
  if(boundary_state == 0) {
    if(analogRead(A0) < 250){ // 빗방울 감지핀의 수위가 250미만이면
      digitalWrite(relay_valve_in, HIGH);
      digitalWrite(relay_valve_out, HIGH);
      boundary_state = 1;
    }                                  
    else { // 빗방울 감지핀의 출력값이 250이상이면
      lcd.setCursor(0,1);
      lcd.print("State: Water In");
      digitalWrite(relay_valve_in, LOW);
      digitalWrite(relay_valve_out, HIGH);
      boundary_state = 0;
    }
  }
  
  if(boundary_state == 0 || temp_ok == 0) {
    prev_time = millis();
  }
  if(boundary_state == 1) {
    lcd.setCursor(0,1);
    lcd.print("State: Heating");
    if(water_temp < 75) { // 75도 보다 작을 때
      digitalWrite(relay_heater, LOW);
    }
    else if(water_temp >= 75) { // 75도 이상일 때
      digitalWrite(relay_heater, HIGH);
      temp_ok = 1;
    }
    if(curr_time-prev_time>180000) { // 75도이상 됬을 때, 3분동안 불림
//      heat_time = heat_time + (curr_time-prev_time) - 5000;
      boundary_state = 0;
      temp_ok = 0;
      cleaner_state = 1;
      prev_time = curr_time;
    }
  }
  
  if(cleaner_state == 0) {
    prev_clean = millis();
  }
  if(cleaner_state == 1) {
    lcd.setCursor(0,1);
    lcd.print("State: Cleaning");
    digitalWrite(relay_heater, HIGH);
    digitalWrite(relay_cleaner1, LOW);
    digitalWrite(relay_cleaner2, LOW);
    if(curr_time-prev_clean>300000) { // 5분동안 초음파작동
      digitalWrite(relay_cleaner1, HIGH);
      digitalWrite(relay_cleaner2, HIGH);
      cleaner_state = 0;
      clean_done = 1;
      prev_clean = curr_time;
    }
  }

  if(clean_done == 0) {
    prev_out = millis();
  }
  if(clean_done == 1) {
    lcd.setCursor(0,1);
    lcd.print("State: Water Out");
    digitalWrite(relay_valve_in, HIGH);
    digitalWrite(relay_valve_out, LOW);
    if(curr_time-prev_out>180000) { // 3분동안 물 뺌
      digitalWrite(relay_valve_out, HIGH);
      clean_done = 0;
      clean_count++;
      if(clean_count > 2) {
        arduino_state = 0;
        digitalWrite(relay_valve_in, HIGH);
        digitalWrite(relay_valve_out, HIGH);
        digitalWrite(relay_heater, HIGH);
        digitalWrite(relay_cleaner1, HIGH);
        digitalWrite(relay_cleaner2, HIGH);
      }
      prev_out = curr_time;
    }
  }
  }
  
//  delay(100); // 센서값 간격 0.1초로 설정
}