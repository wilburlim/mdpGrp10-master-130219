package mdp.common;

public class Vector2 { //object for storing coordinates i and j for a vector point

    private int _i;
    private int _j;

    public Vector2(int i, int j) {
        _i = i;
        _j = j;
    }

    public int i() {
        return _i;
    }

    public int j() {
        return _j;
    }

    public void i(int i) {
        _i = i;
    }

    public void j(int j) {
        _j = j;
    }

    @Override
    public String toString() {
        //System.out.println("(" + _i + ", " + _j + ")");
    	return "(" + _i + ", " + _j + ")"; //string of format (i, j)
    }

    public boolean equals(Vector2 coord) {
        return coord.i() == _i && coord.j() == _j;
    }

    public void add(Vector2 coord) {
        _i += coord.i();
        _j += coord.j();
    }

    public void multiply(int multiplier) {
        _i *= multiplier;
        _j *= multiplier;
    }

    public Vector2 fnAdd(Vector2 coord) {
        return new Vector2(_i + coord.i(), _j + coord.j());
    }

    public Vector2 fnMultiply(int multiplier) {
        return new Vector2(_i * multiplier, _j * multiplier);
    }
    
    public Vector2(String x) {
    	// Replacing every non-digit number 
        // with a space(" ") 
        x = x.replaceAll("[^\\d]", " "); 
  
        // Remove extra spaces from the beginning 
        // and the ending of the string 
        x = x.trim(); 
  
        // Replace all the consecutive white 
        // spaces with a single space 
        x = x.replaceAll(" +", " "); 
        
        String[] integerStrings = x.split(" "); 
        // Splits each spaced integer into a String array.
        int[] integers = new int[integerStrings.length]; 
        // Creates the integer array.
        for (int i = 0; i < integers.length; i++){
          integers[i] = Integer.parseInt(integerStrings[i]); 
           //Parses the integer for each string.
         }
        
    	_i = integers[0];
        _j = integers[1];
        
    }
    

    @Override
    public boolean equals(Object obj) {
        return equals((Vector2) obj);
    }
}
