package org.ligi.gobandroid_hd.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity;
import org.ligi.tracedroid.logging.Log;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;

public class UnzipSGFsDialog {
	
	public static class Decompress { 
		  private InputStream _zipFile; 
		  private String _location; 
		 
		  public Decompress(InputStream zipFile, String location) { 
		    _zipFile = zipFile; 
		    _location = location; 
		 
		    _dirChecker(""); 
		  } 
		 
		  public void unzip() { 
			  try  { 
				  InputStream fin = _zipFile; 
				  ZipInputStream zin = new ZipInputStream(fin); 
				  ZipEntry ze = null; 
				  while ((ze = zin.getNextEntry()) != null) { 
		    	  Log.i("Decompress" + "unzip" + ze.getName()); 
		        if(ze.isDirectory()) { 
		          _dirChecker(ze.getName()); 
		        } else { 
		          FileOutputStream fout = new FileOutputStream(_location + ze.getName()); 
		          for (int c = zin.read(); c != -1; c = zin.read()) { 
		            fout.write(c); 
		          } 
		 
		          zin.closeEntry(); 
		          fout.close(); 
		        } 
		         
		      } 
		      zin.close(); 
		    } catch(Exception e) { 
		      Log.e("Decompress", "unzip", e); 
		    } 
		 
		  } 
		 
		  private void _dirChecker(String dir) { 
		    File f = new File(_location + dir); 
		 
		    if(!f.isDirectory()) { 
		      f.mkdirs(); 
		    } 
		  } 
		} 
	 /**
     * 
     * @param activity
     * @param autoclose - if the alert should close when connection is established
     * 
     */
    public static void show(GobandroidFragmentActivity activity,Intent intent_after_finish) {
    		
    	ProgressDialog dialog = ProgressDialog.show(activity, "", 
                "Unziping SGF's. Please wait...", true);

    	 class AlertDialogUpdater implements Runnable {

             private ProgressDialog myProgress;
             private GobandroidFragmentActivity activity;
             private Intent intent_after_finish;
             
             public AlertDialogUpdater(GobandroidFragmentActivity activity,ProgressDialog progress,Intent intent_after_finish) {
            	 this.activity=activity;
            	 this.intent_after_finish=intent_after_finish;
                 myProgress=progress;
             }

             public void run() {
            	 Resources resources = activity.getResources(); 
                 
                 InputStream is = resources.openRawResource(R.raw.sgf_pack);
                 new Decompress(is,activity.getSettings().getSGFBasePath()).unzip();
                 
                 
            	 myProgress.dismiss();
            	 activity.startActivity(intent_after_finish);
             }
    	 }
    	 new Thread(new AlertDialogUpdater(activity,dialog,intent_after_finish)).start();
    	 
    }
}
