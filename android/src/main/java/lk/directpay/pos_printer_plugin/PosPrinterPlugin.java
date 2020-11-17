package lk.directpay.pos_printer_plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.neptunelite.api.NeptuneLiteUser;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** PosPrinterPlugin */
public class PosPrinterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
    Activity activity;
    Context context;
    private IPrinter printer;
    private static IDAL dal;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "pos_printer_plugin");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
      Log.d("printReceipt","method"+call.method);
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } 
    if (call.method.equals("printReceipt")){
        Log.d("printReceipt","method Called");
        String dateTime = call.argument("dateTime");
        String merchantName = call.argument("merchantName");
        String reference = call.argument("reference");
        String status = call.argument("status");
        String amount=call.argument("amount");

      printSlip(dateTime,merchantName,reference,status,amount);
      result.success("Success");
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public void printSlip(final String dateTime, final String merchantName, final String reference, final String status, final String amount)
  {
      new Thread(new Runnable()
      {
          public void run()
          {
              TesterPageComposing testerPageComposing = new TesterPageComposing(context, dateTime,merchantName,reference,status,amount);
              testerPageComposing.run();
              Bitmap bitmap = testerPageComposing.getBitmap();
              try
              {
                  dal = NeptuneLiteUser.getInstance().getDal(context);
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
              printer = dal.getPrinter();

              try
              {
                  printer.init();
                  printer.setGray(1);
                  printer.printBitmap(bitmap);
                  printer.start();
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
          }
      }).start();
  }
}
