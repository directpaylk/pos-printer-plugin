
import 'dart:async';

import 'package:flutter/services.dart';

class PosPrinterPlugin {
  static const MethodChannel _channel =
      const MethodChannel('pos_printer_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
