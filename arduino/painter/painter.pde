
// ERROR CONSTANTS
const int error_out_of_range = 0;

const int pin_dbg_led = 13;

const int pin_m1_step = 2;
const int pin_m1_dir = 3;
const int pin_m2_step = 4;
const int pin_m2_dir = 5;

const int pin_led_r = 9;
const int pin_led_g = 10;
const int pin_led_b = 11;

// TODO: Set correct values!
const int max_x = 200;
const int max_y = 200;

const int max_r = 255;
const int max_g = 200;
const int max_b = 110;

const int len_arm_1 = 180;
const int len_arm_2 = 180;

String cmdBuf = "";
const char cmdStart = '[';
const char cmdEnd = ']';
const char paramListStart = '(';
const char paramListEnd = ')';
const char paramSeparator = ',';

int mc_m1 = 0;
int mc_m2 = 0;

void setup() {
  Serial.begin(9600);
  
  pinMode(pin_dbg_led, OUTPUT); 
  
  pinMode(pin_m1_step, OUTPUT); 
  pinMode(pin_m1_dir, OUTPUT); 
  pinMode(pin_m2_step, OUTPUT); 
  pinMode(pin_m2_dir, OUTPUT);
  
  pinMode(pin_led_r, OUTPUT);
  pinMode(pin_led_g, OUTPUT);
  pinMode(pin_led_b, OUTPUT);  
}

void loop() {
  if (Serial.available()) {
    char c = Serial.read();
    
    if (cmdBuf.length() == 0 && c == cmdStart) {
      cmdBuf += c;
    } else if (cmdBuf.length() > 0) {
      cmdBuf += c;
      
      if (c == cmdEnd) {
        executeCommand(cmdBuf);
        cmdBuf = "";
      }
    }
    
    
  }
}

void executeCommand(String raw_cmd) {
  String cmd = raw_cmd.substring(1, raw_cmd.length() - 1);
  int paramListStartPos = cmd.indexOf(paramListStart);
  int paramListEndPos = cmd.indexOf(paramListEnd);
  
  String verb = cmd.substring(0,paramListStartPos).trim();
  String paramList = cmd.substring(paramListStartPos+1, paramListEndPos);
  
  if (verb.equals("blink_led")) {
    String times_raw = getParameter(paramList, 0);
    blink_led(getInt(times_raw));
  } else if (verb.equals("moveToCoord")) {
    String x_raw = getParameter(paramList, 0);
    String y_raw = getParameter(paramList, 1);
    moveToCoord(getInt(x_raw), getInt(y_raw));
  } else if (verb.equals("setColor")) {
    String r_raw = getParameter(paramList, 0);
    String g_raw = getParameter(paramList, 1);
    String b_raw = getParameter(paramList, 2);
    setColor(getInt(r_raw), getInt(g_raw), getInt(b_raw));
  } else if (verb.equals("step")) {
    String nMotor1_raw = getParameter(paramList, 0);
    String nMotor2_raw = getParameter(paramList, 1);
    doStep(getInt(nMotor1_raw), getInt(nMotor2_raw));
  } else if (verb.equals("resetMC")) {
    resetMC();
  } else if (verb.equals("stepToMC")) {
    String nMotor1_raw = getParameter(paramList, 0);
    String nMotor2_raw = getParameter(paramList, 1);
    stepToMC(getInt(nMotor1_raw), getInt(nMotor2_raw));
  } else {
    Serial.println("Unknown verb '" + verb + "'.");
  }
}

String getParameter(String paramList, int paramNo) {
  int lastSeparatorPos = 0;
  for (int i=0; i<paramNo; ++i) {
    lastSeparatorPos = paramList.indexOf(paramSeparator, lastSeparatorPos) + 1;
  }
  
  int paramStart = lastSeparatorPos;
  int paramEnd = paramList.indexOf(paramSeparator, paramStart);
  if (paramEnd <= 0) {
    paramEnd = paramList.length();
  }
  
  String parameter = paramList.substring(paramStart, paramEnd);  
  return parameter;
}

void blink_led(int times) {  
  for (int i=0; i<times; ++i) {
    digitalWrite(pin_dbg_led, HIGH);
    delay(250);
    digitalWrite(pin_dbg_led, LOW);
    delay(250);
  }
}

void moveToCoord(int x, int y) {
 String c = "";
 if (x < 0 || x > max_x || y < 0 || y > max_y) {
   Serial.println(c + "Coords out of range. Max range is 0.." + max_x + " and 0.." + max_y + " for x and y, respectively.");
 }
 
 Serial.println(c + "Moving to coords " + x + ", " + y + " ... ");
}

int getInt(String text)
{
  char temp[20];
  text.toCharArray(temp, 19);
  int x = atoi(temp);
  if (x == 0 && text != "0")
  {
    x = -1;
  }
  return x;
} 

void stepToMC(int motorCoord1, int motorCoord2) {
  doStep(motorCoord1 - mc_m1, motorCoord2 - mc_m2);
  mc_m1 = motorCoord1;
  mc_m2 = motorCoord2;
}

void doStep(int nSteps1, int nSteps2) {
  String c = "";
  
  if(nSteps1 < 0){
    digitalWrite(pin_m1_dir, LOW);
  }
  else{
    digitalWrite(pin_m1_dir, HIGH);
  }
  
  if(nSteps2 < 0){
    digitalWrite(pin_m2_dir, LOW);
  }
  else{
    digitalWrite(pin_m2_dir, HIGH);
  }  
  
  nSteps1 = abs(nSteps1);
  nSteps2 = abs(nSteps2);  
  
  Serial.println(c + "Doing " + nSteps1 + " steps on pins " + pin_m1_dir + " (dir) and " + pin_m1_step + " (step)  ...");
  Serial.println(c + "Doing " + nSteps2 + " steps on pins " + pin_m2_dir + " (dir) and " + pin_m2_step + " (step)  ...");  
  
  
  for (int i=0; i<max(nSteps1,nSteps2); ++i) {
    if (i<nSteps1) {
      digitalWrite(pin_m1_step, HIGH);
    }
    if (i<nSteps2) {
      digitalWrite(pin_m2_step, HIGH);
    }
    delayMicroseconds(50);
    digitalWrite(pin_m1_step, LOW);
    digitalWrite(pin_m2_step, LOW);
    delay(100);
    //Serial.println(c + "Step " + i);
  }
}

// void doStep(int dir, int nMotor, int nSteps)
// {
//  String c = "";
//  
//  int pinDir = -1;
//  int pinStep = -1;
//  
//  if(nMotor == 1){
//    pinDir = pin_m1_dir;
//    pinStep = pin_m1_step;
//  }
//  else if(nMotor == 2){
//    pinDir = pin_m2_dir;
//    pinStep = pin_m2_step;
//  }
//  else{
//    return;
//  }
//  
//  if(dir == 0){
//    digitalWrite(pinDir, HIGH);
//  }
//  else{
//    digitalWrite(pinDir, LOW);
//  }
//  
//  Serial.println(c + "Doing " + nSteps + " steps on pins " + pinDir + " (dir) and " + pinStep + " (step)  ...");
//  
//  for (int i=0; i<nSteps; ++i) {
//    digitalWrite(pinStep, HIGH);
//    delayMicroseconds(50);
//    digitalWrite(pinStep, LOW);
//    delay(100);
//    //Serial.println(c + "Step " + i);
//  }

  
  
// }

void resetMC() {
  mc_m1 = 0;
  mc_m2 = 0;
}

void setColor(int r, int g, int b) {
  analogWrite(pin_led_r, map(r, 0,255, 0,max_r));
  analogWrite(pin_led_g, map(g, 0,255, 0,max_g));
  analogWrite(pin_led_b, map(b, 0,255, 0,max_b));  
}

// ALPHA
float getAngleM1(int x, int y) {
  float d = sqrt(x*x+y*y);
  
  return acos(-((len_arm_2*len_arm_2 - len_arm_1*len_arm_1 - d*d) / (2*len_arm_1*d)));
}


// BETA
float getAngleM2(int x, int y) {
  float d = sqrt(x*x+y*y);
  
  return acos(-((d*d - len_arm_2*len_arm_2 - len_arm_1*len_arm_1) / (2*len_arm_1*len_arm_2)));
}

int angleToMC(float angle) {

}
