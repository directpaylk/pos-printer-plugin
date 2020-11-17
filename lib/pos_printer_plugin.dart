
import 'dart:async';

import 'package:flutter/services.dart';

class PosPrinterPlugin {
  static const MethodChannel _channel =
      const MethodChannel('pos_printer_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> printReceipt(String dateTime, String merchantName, String reference, String status,String amount) async {
    print("printReceiptCalled in main");
    try {
      final String result = await _channel
          .invokeMethod('printReceipt', {'amount':amount,'dateTime': dateTime,'merchantName':merchantName,'reference':reference,'status':status});
      print("printReceiptCalled in main result"+result);
      return result;
    } catch (e) {
      print(e);
    }
    return "print failed";
  }
}
