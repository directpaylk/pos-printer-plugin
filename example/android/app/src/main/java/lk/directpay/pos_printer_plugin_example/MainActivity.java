package lk.directpay.pos_printer_plugin_example;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import lk.directpay.pos_printer_plugin.TesterPageComposing;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "pos_printer_plugin";
    Context context;
//    private IPrinter printer;
//    private static IDAL dal;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {

        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            // TODO
                            if (call.method.equals("printReceipt")) {
                                Log.d("printReceipt","method"+call.method);
                                Log.d("printReceipt","method Called");
                                String dateTime = call.argument("dateTime");
                                String merchantName = call.argument("merchantName");
                                String reference = call.argument("reference");
                                String status = call.argument("status");
                                String amount=call.argument("amount");

                                printSlip(dateTime,merchantName,reference,status,amount);
                                result.success("Success");
                                result.success("");

                                } else {
                                    result.error("UNAVAILABLE", "Print method not available.", null);
                                }

                        }
                );
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
//                try
//                {
//                    dal = NeptuneLiteUser.getInstance().getDal(context);
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                printer = dal.getPrinter();
//
//                try
//                {
//                    printer.init();
//                    printer.setGray(1);
//                    printer.printBitmap(bitmap);
//                    printer.start();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }
}
