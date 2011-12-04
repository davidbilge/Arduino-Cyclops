
// ERROR CONSTANTS
const int error_out_of_range = 0;

const int pin_dbg_led = 13;

// TODO: Set correct values!
const int max_x = 200;
const int max_y = 200;

String cmdBuf = "";
const char cmdStart = '[';
const char cmdEnd = ']';
const char paramListStart = '(';
const char paramListEnd = ')';
const char paramSeparator = ',';

void setup() {
  Serial.begin(9600);
  pinMode(pin_dbg_led, OUTPUT);  
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

void setColor(int r, int g, int b) {

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

