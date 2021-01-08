# Cross-platform cryptography

## Elliptic key string signature (ECDSA) encoding

You may ask why I'm providing two separate signature versions using the same curve - the answer is easy: the use different signature encoding. As both are used very often there is no real "standard" and I'm trying to explain where the differences are and how to find out what encoding is in use in a sample case.

#### Very important for the following information: we are talking about a signature that was generated with a 256 bit long Elliptic Curve like "SECP256R1" = "P-256" = "PRIME256V1".

### How do I know which encoding was used to generate the signature?

The answer is easy: simply Base64 decode the signature string and count the bytes - if the number is **64 bytes** then we do have an **IEEE-P1363 encoded signature**. If the number is **higher then 64 bytes** it will be a **DER encoded signature**.

### How do the signatures look like on byte basis?

After Base64 decoding we get these byte structures:

IEEE P1363 signature (Base64: KyG+2ENIyo0k1o89/fVuHkfweI7VezwShKS0p9C9+uv0EqXdsK/S64lD8W351raIHdF3WSDKgQKbPJO5UYjTMA==)

```plaintext
2B 21 BE D8 43 48 CA 8D 24 D6 8F 3D FD F5 6E 1E 
47 F0 78 8E D5 7B 3C 12 84 A4 B4 A7 D0 BD FA EB 
F4 12 A5 DD B0 AF D2 EB 89 43 F1 6D F9 D6 B6 88 
1D D1 77 59 20 CA 81 02 9B 3C 93 B9 51 88 D3 30 
```

DER signature (Base64: MEUCICshvthDSMqNJNaPPf31bh5H8HiO1Xs8EoSktKfQvfrrAiEA9BKl3bCv0uuJQ/Ft+da2iB3Rd1kgyoECmzyTuVGI0zA=)

```plaintext
30 45 02 20 2B 21 BE D8 43 48 CA 8D 24 D6 8F 3D 
FD F5 6E 1E 47 F0 78 8E D5 7B 3C 12 84 A4 B4 A7 
D0 BD FA EB 02 21 00 F4 12 A5 DD B0 AF D2 EB 89 
43 F1 6D F9 D6 B6 88 1D D1 77 59 20 CA 81 02 9B 
3C 93 B9 51 88 D3 30                            
```

### Is there any structure within the data?

The short answer is yes and in a longer answer you will see that <u>both signatures are identical</u> means the contain the same values:

I'm splitting up the **IEEE P1363 encoded signature** (64 bytes long) into 2 byte arrays of each 32 byte length, the first byte array is named "R" and the second one "S":

```plaintext
R (32 byte):  2B 21 BE D8 43 48 CA 8D 24 D6 8F 3D FD F5 6E 1E 47 F0 78 8E D5 7B 3C 12 84 A4 B4 A7 D0 BD FA EB 
S (32 byte):  F4 12 A5 DD B0 AF D2 EB 89 43 F1 6D F9 D6 B6 88 1D D1 77 59 20 CA 81 02 9B 3C 93 B9 51 88 D3 30 

```

Now I'm splitting up the **DER encoded signature** that contains some extra data:

```plaintext
DER structure (1 byte):    30
Length following (1 byte): 45
Data marker (1 byte):      02
Data length (1 byte):      20
R data (32 byte = x20):    2B 21 BE D8 43 48 CA 8D 24 D6 8F 3D FD F5 6E 1E 47 F0 78 8E D5 7B 3C 12 84 A4 B4 A7 D0 BD FA EB 
Data marker (1 byte):      02
Data length (1 byte):      21
S data (33 byte = x21):    00 F4 12 A5 DD B0 AF D2 EB 89 43 F1 6D F9 D6 B6 88 1D D1 77 59 20 CA 81 02 9B 3C 93 B9 51 88 D3 30

```

Comparing the "R" and "S" you will notice that they have the same value except an extra "00" byte for the "S" value. The extra byte is necessary to retrieve a positive value in the following verification process. 

The other data in the DER encoded signature are just for information and better reading.

As the "R" and "S" values in the IEEE P1363 encoding are simply concatenated this encoding often is called **raw encoding** or **R|S encoding**.

If you may think - "*wow, that seems easy to convert between each*" will find out that there are a lot of hick ups and believe me - it is more easy to directly choose the fitting algorithm.

Last update: Jan. 08th 2021

Back to the main page: [readme.md](../readme.md)