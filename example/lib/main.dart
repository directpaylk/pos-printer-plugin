import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pos_printer_plugin/pos_printer_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _printed='print failed';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await PosPrinterPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child:Column(children:[Text('Running on: $_platformVersion\n'),
            Row(
              children: <Widget>[
                RaisedButton(
                  child: Text("Print receipt"),
                  onPressed: () {
                    this._printReceipt();
                  },
                ),
                SizedBox(
                  width: 10,
                ),
                Text("Printed:"+_printed)
              ],
            ),
          ] ),
        ),
      ),
    );
  }
  Future<String> _printReceipt() async {
    print("printReceiptCalled");
    try {
      String printed = await PosPrinterPlugin.printReceipt("2020-11-17 18:37", "Test", "012", "SUCCESS","100.00","R12444244455","12443444343");
      setState(() {
        this._printed = printed;
      });
    } on PlatformException catch (e) {
      print(e);
      setState(() {
        this._printed = "print failed";
      });
    }

    return _printed;
  }
}
