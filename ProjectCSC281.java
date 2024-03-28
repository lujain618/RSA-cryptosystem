package lcg; 
import java.util.*; 
import java.math.*; 

// main class 
public class ProjectCSC281 {


//class for public key; 

static class PublicKey {
private long n; 
private long e; 

public PublicKey(long n, long e){
this.n = n;
this.e = e; }

public long getN() {
return n; }

public long getE() {
return e; } 

}

//class for private key
static class PrivateKey {
private long n; 
private long d; 

public PrivateKey(long n, long d) {
this.n = n; 
this.d = d; }

public long getN() {
return n; }

public long getD() {
return d; }

}

//class for key pair
static class KeyPair {
private PublicKey publicKey; 
private PrivateKey privateKey; 

public KeyPair(PublicKey pub, PrivateKey prv) {
publicKey = pub; 
privateKey = prv; }

public PublicKey getPublicKey() {
return publicKey; }

public PrivateKey getPrivateKey() {
return privateKey; }
}

public static void main (String[] args) {

System.out.println("Hi there! This is the main method");
System.out.println("calling generateKeys");
KeyPair keyPair = RSAKeyGeneration();
PublicKey pubKey = keyPair.getPublicKey();
PrivateKey prvKey = keyPair.getPrivateKey();
long e = keyPair.getPublicKey().getE();
long n = keyPair.getPublicKey().getN();
long d = keyPair.getPrivateKey().getD();

String str = "hello"; 
System.out.println("setting plaintext to: " + str);
System.out.println("calling encrypt...");
long[] ciphertext = encrypt(str,e, n) ;

System.out.println("calling decrypt on encrypt output...");
String decryptedMessage = decrypt(ciphertext, d, n);

System.out.println("making sure decryptedText and plaintext are equalsIgnoreCase...");
if(str.equalsIgnoreCase(decryptedMessage))
System.out.println("Yes! they are");
else 
System.out.println("No! they are not");

System.out.println("bye now! --main method"); }

// Method 1 Linear Congruential Generator (LCG) :
    
public static int[] LCG(int seed, int quantity) {

System.out.println("Hi there! This is LCG method, I am called with\n(seed=" + seed + " quantity=" + quantity + ")\nand I have those initialized local variables:\n(A = 1664525 C = 1013904223 M = 32767)");

int A = 1664525 ;
int C = 1013904223 ;
int M = 32767 ;  
        
int[] Random = new int[quantity];
      
for (int i = 0 ; i < quantity ; i++) {
        
seed = (A * seed + C) % M ;
            
if (seed < 0) {  
seed = seed + M; 
}
            
Random[i] = seed;  
}

System.out.println("I generated " + quantity + " random numbers, and I made them all positives!");
for (int i=0; i<100; i++)
System.out.print(Random[i] +", ");
System.out.println();
System.out.println("bye now! --LCG method");

return Random ;
        
      
}


// Method 2 Miller-Rabin Primality Test :

public static boolean Checking_primelity(long num, long random_num){

long temp;
long exponent ;
exponent = num-1;

while ((exponent &1)==0)
exponent = exponent>>1;
temp = modularExponentiation(random_num,exponent,num);
if (temp ==1)
return true;

else 
while (exponent < num-1){
 temp = modularExponentiation(random_num,exponent,num);
 if (temp==num-1)
 return true;
 
else 
exponent = exponent<<1;
}
return false;
}


public static boolean millerRabinTest(long n, int k) {
	for(int i=0; i<k; i++) {
	long a = (long) (Math.random()*(n-2))+2; 
	if (Checking_primelity(n,a))
		return true;	
	}
	return false;
}


// Method 3 RSA Key Generation :


public static long extendedEuclideanAlgorithim(long e, long m) {

long x = 0, y = 1, coefficientX = 1, coefficientY = 0, temp;
        
while (m != 0) {
long q = e / m;
long r = e % m;

e = m;
m = r;

temp = x;
x = coefficientX - q * x;
coefficientX = temp;

temp = y;
y = coefficientY - q * y;
coefficientY = temp;
        }

if (e != 1) 
return 0; // Modular multiplicative inverse doesn't exist


return coefficientX;
}


public static KeyPair RSAKeyGeneration() {

System.out.println("I will be generating 100 random numbers using LCG method");
System.out.println("calling LCG...");
int[] list_random = LCG(5,100);

System.out.println("back to generateKeys, now I will examine the random numbers and assign p to the first number\nthat passes millerRabinTest\nq to the second number (if it is not equal to p... duh!)");


int[] prime_num = new int[2];
int count = 0; 

for(int i =0; i<100; i++) {
if(millerRabinTest(list_random[i],10)){

System.out.print((count == 0)? "p" : "q");
System.out.println(" is " + list_random[i] +" this is the " + (i+1) + " element in the random list"); 
prime_num[count] = list_random[i];
count++;

if(count == 2)
break; 

} }



long p = prime_num[0];
long q = prime_num[1];

long n = p*q;
long phi = (p-1)*(q-1);

System.out.println("I calculated phi: " + phi);

long e = 65537; 
System.out.println("I set e: " + e);
long d = extendedEuclideanAlgorithim(e,phi);
System.out.println("I called extendedEuclideanAlgorithim, and got d to be: " + d);

PublicKey publicKey = new PublicKey(n,e);
PrivateKey privateKey = new PrivateKey(n,d);

System.out.println("finally, I am creating an instance of KeyPair class as:\nKeyPair(new PublicKey(n,e), new PrivateKey(n,d))\nand returning it. Bye now! --generateKeys method");

return new KeyPair(publicKey,privateKey);

}

// Method 4 Modular Exponentiaion 


/*  method encrypt */   
public static long[] encrypt(String message, long e, long n) {
System.out.println("Hi there! This is encrypt method");

long[] intarray = String_to_intArray(message);
long[] encrypt = new long [intarray.length];
long exponent = (long)e;
long mod = (long)n;
long base = 0;
for(int i = 0; i<intarray.length; i++) {
base = (long)intarray[i];
encrypt[i] = modularExponentiation(base,exponent,mod);
}
System.out.println("encryptedValues: ");
printArray(encrypt);

System.out.println("bye now! --encrypt method");
return encrypt;
}

/*  method String_to_intArray */
public static long[] String_to_intArray(String message) {
char[] a = message.toCharArray();
long[] b = new long[a.length];
for (int i = 0; i < a.length; i++) 
b[i] = ((long)a[i])-96;
System.out.println("converting string to int:");
printArray(b);
return b;
}

/* method IntArray_to_String */
public static String IntArray_to_String(long[] longArray){
char [] charArray = new char [longArray.length];
for (int i = 0; i<longArray.length; i++) {
charArray[i] = (char)((longArray[i])+96);
}
String message = new String(charArray);
System.out.println("ArrayToString " + message);
return message;
}
   

/*  method decrypt */
public static String decrypt(long[] ciphertext,long d,long n) {
System.out.println("Hi there! This is decrypt method");
long [] longArray = new long[ciphertext.length];
long exponent = (long)d;
long mod = (long)n;
long base = 0;
for(int i = 0; i<ciphertext.length; i++) {
base = ciphertext[i];
longArray[i] = modularExponentiation(base,exponent,mod);
}
System.out.println("decryptedValues:");
printArray(longArray);
String original = IntArray_to_String(longArray);
System.out.println("bye now! --decrypt method");
return original;
}

/*  method modularExponentiation*/
public static long modularExponentiation(long base, long exponent, long modulos) {
if (modulos==1)
return 0;
long result = 1;
base = base%modulos;
while(exponent>0) {
if((exponent & 1)==1)
result = (result*base)%modulos;
exponent = exponent>>1;
base = (base*base)%modulos;
}
return result;
}
   
   
/* method printArray */
public static void printArray(long[] array) {
for (int i = 0; i<array.length; i++) 
System.out.printf("%-10d",array[i]);
System.out.printf("%n");
} 

}
