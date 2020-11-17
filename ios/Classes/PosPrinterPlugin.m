#import "PosPrinterPlugin.h"
#if __has_include(<pos_printer_plugin/pos_printer_plugin-Swift.h>)
#import <pos_printer_plugin/pos_printer_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "pos_printer_plugin-Swift.h"
#endif

@implementation PosPrinterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPosPrinterPlugin registerWithRegistrar:registrar];
}
@end
