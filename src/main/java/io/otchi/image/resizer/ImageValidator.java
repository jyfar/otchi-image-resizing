package io.otchi.image.resizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator{

   private Pattern pattern;
   private Matcher matcher;

   private static final String IMAGE_PATTERN =
                "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

   public ImageValidator(){
	  pattern = Pattern.compile(IMAGE_PATTERN);
   }

   /**
   * Validate image with regular expression
   * @param image image for validation
   * @return true valid image, false invalid image
   */
   public boolean validate(final String image){

	  matcher = pattern.matcher(image);
	  return matcher.matches();

   }
   
   public String getImageType(final String image){
	   String extension = "";
	   int i = image.lastIndexOf('.');
	   if (i > 0) {
	       extension = image.substring(i+1);
	   }
	   return extension;
   }
}
