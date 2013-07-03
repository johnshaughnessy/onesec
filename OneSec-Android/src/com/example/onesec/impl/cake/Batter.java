package com.example.onesec.impl.cake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.example.onesec.Kitchen;
import com.example.onesec.impl.second.Second;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

public class Batter {

	private List<String> idList;
	private String id;
	
	public Batter()
	{
		idList = new ArrayList<String>();
		id = generateId();
	}
	
	private String generateId() {
		return "cake" + (new Random()).nextInt();
	}

	public void addSecond(Second second)
	{
		idList.add(second.getId());
	}
	
	public void removeSecond(Second second)
	{
		idList.remove(second);
	}
	
	public void move(int start, int end)
	{
		String idToMove = idList.get(start);
		// Shift all the seconds forward
		for (int i = start; i < end; ++i){
			idList.set(i, idList.get(i+1));
		}
		idList.set(end, idToMove);
	}
	
	public Cake bake() throws IOException{
		Uri cakeVidUri = bakeCakeAndReturnUri();
		Uri cakeThumbUri = makeThumbnailAndReturnUri(cakeVidUri);
		return new Cake(this, cakeVidUri, cakeThumbUri);
	}



	private Uri bakeCakeAndReturnUri() throws IOException {
		// Set up Tracks
		List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();
        
        for (Movie m : getMovieList()) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }
        
        // Use tracks to concatenate into result
        Movie result = new Movie();
        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }
        
        // Build .mp4
        Container out = new DefaultMp4Builder().build(result);

        
        File cakeStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "OneSec/Cakes");
        
        // Create the storage directory if it does not exist
        if (! cakeStorageDir.exists()){
            if (! cakeStorageDir.mkdirs()){
                Log.d("OneSec", "failed to create directory");
                return null;
            }
        }
        
        File output = new File(cakeStorageDir.getPath() + File.separator +
                id + ".mp4");
        FileOutputStream fos = new FileOutputStream(output);
        out.writeContainer(fos.getChannel());
        fos.close();
		return null;
	}
	
	private List<Movie> getMovieList(){
		List<Movie> movies = new ArrayList<Movie>();
		for( String id : idList ){
			try {
				movies.add(Kitchen.getSecond(id).getMovie());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return movies;
	}
	
	private Uri makeThumbnailAndReturnUri(Uri cakeVidUri) {
		Uri thumbnailUri = convertVidUri(cakeVidUri);
		
		File thumbnailFile = new File(thumbnailUri.getPath());
		
		if(!thumbnailFile.exists()){
			Bitmap thumbBmp = ThumbnailUtils.createVideoThumbnail(cakeVidUri.getPath(), 3);
			FileOutputStream out;
			try{
				out = new FileOutputStream(thumbnailFile);
				thumbBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return thumbnailUri;
	}
	
	private Uri convertVidUri(Uri vidUri) {
		
		return null;
	}
	
	public String getId(){
		return id;
	}
	
}
