/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tempqueueproducer;

/**
 *
 * @author SeenamZaSodaSingha
 */
public class GetPrime {
    
    int min = 0, max = 0, cnt = 0;
    
    public String GetPrime(String str){
        String[] strArr = str.split(",");
        min = Integer.parseInt(strArr[0]);
        max = Integer.parseInt(strArr[1]);
        for (int i = min;min <= max; i++){
            cnt = isPrime(i) ? cnt++ : cnt;
        }
        return "The number of primes between " + min + " and " + max + " is " + cnt + ".";
    }

    private boolean isPrime(int n) {
        int i;
        for (i = 2; i * i <= n; i++) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
