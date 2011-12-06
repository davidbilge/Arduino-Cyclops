const int pin_step_1 = 2;
const int pin_dir_1 = 3;

void setup() {
  pinMode(pin_step_1, OUTPUT);
  pinMode(pin_dir_1, OUTPUT);
  digitalWrite(pin_dir_1, HIGH);
}

void loop() {
  digitalWrite(pin_step_1, HIGH);
  delayMicroseconds(50);
  digitalWrite(pin_step_1, LOW);
  delayMicroseconds(200);
}
