
import 'dart:async';

import 'package:flutter/services.dart';

class PosPrinterPlugin {
  static const MethodChannel _channel =
      const MethodChannel('pos_printer_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> printReceipt(String dateTime, String merchantName, String reference, String status) async {
    try {
      final bool result = await _channel
          .invokeMethod('printReceipt', {'dateTime': dateTime,'merchantName':merchantName,'reference':reference,'status':status});
      return result;
    } catch (e) {
      print(e);
    }
    return false;
  }
}
