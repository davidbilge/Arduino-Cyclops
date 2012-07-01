Configuring in Eclipse
=======================
To make the `*.dll`s for RxTx known to Eclipse, add them as a "native dependency" to the "Maven Dependencies" library:
* Open project properties
* Navigate to "Java Build path"
* Switch to the "Libraries" tab
* Expand the "Maven Dependencies" node
* Select "Native library location" and click "Edit..."
* Choose the folder containing the rxtxSerial.dll (or the respective `.so` file)