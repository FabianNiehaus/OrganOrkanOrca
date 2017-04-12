/**
 * 
 */
package util;

/**
 * @author Fabian
 *
 */
public class StringComparator {
	
	/**
	 * Vergleicht die "Größe" von 2 Strings anhand der int-Repräsentation der einzelnen Zeichen
	 * @param s1 String 1
	 * @param s2 String 2
	 * @return <b>True</b>, wenn s1 größer (oder länger wenn gleich) als s2; <b>false</b> wenn s2.
	 */
	public static boolean compare(String s1, String s2) {
		int s1_length = s1.length();
		int s2_length = s2.length();
		
		int i = 0;
		
		while(true){
			if(i < s1.length() && i < s2.length()){
				if(s1.charAt(i) > s2.charAt(i)){
					return true;
				} else if (s1.charAt(i) < s2.charAt(i)) {
					return false;
				}
				i++;
			} else {
				if(s1_length > s2_length){
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
