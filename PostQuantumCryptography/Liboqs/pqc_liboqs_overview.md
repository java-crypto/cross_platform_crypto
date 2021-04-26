# Cross-platform cryptography

## Post Quantum Cryptography (PQC) with library Liboqs overview

For a general overview about Post Quantum Computing I recommend to read my article [PQC overview page](pqc_overview.md). This page is about a specialized library that computes nearly all algorithms that are part of round 3 of a competition to find the new standard algorithm(s) for a secure key exchange and signature.

More details about the competition can be found here: [NIST Post-Quantum Cryptography PQC Round 3  Submissions](https://csrc.nist.gov/projects/post-quantum-cryptography/round-3-submissions).

For my PQC-project I tried to find libraries that run the competition algorithms but it is very hard to find them (no matter in which framework I searched). The only library that runs nearly all algorithms is the one from the [**Open Quantum Safe (OQS) project**](https://openquantumsafe.org/), the source code is available on GitHub: [https://github.com/open-quantum-safe](https://github.com/open-quantum-safe).

The core library is written in "C" and there are bindings available ("language wrapper") for the frameworks C++, Go, Java, .Net, Python and Rust. Sounds good to me but there is on point that I can't solve on my own: you need to compile the library on your own and **there are no downloadable compiled versions available**.

With my limited resources and lack of knowledge I'm not been able to master this hurdle. But with the kindly help of the OpenQuantumSafe-team I got the information that there is a "ready to use" docker file available that can be used with Java to build the library and run the tests provided in the Java wrapper [liboqs-java](https://github.com/open-quantum-safe/liboqs-java).

To make it short: I installed docker in a vmWare image of Ubuntu, followed the instructions to download and install the core lib, compiled it and run the algorithm tests from liboqs-java successfully. The next step was to clone the original GitHub repository of liboqs-java and then I changed the tests to get a lot of details and I'm providing them in the following articles.

### Do you provide your source files to let me get the details as well?

In my GitHub repository [PostQuantumCryptography with Liboqs: JavaChanges](https://github.com/java-crypto/cross_platform_crypto/tree/main/PostQuantumCryptography/Liboqs/JavaChanges) I'm providing the changed and appended files so you are been able to run this as well. 

For each algorithm I created a log file with all details, get all log files in my GitHub repository [PostQuantumCryptography with Liboqs: LogFiles](https://github.com/java-crypto/cross_platform_crypto/tree/main/PostQuantumCryptography/Liboqs/LogFiles).

### Do you have examples of the algorithms?

Exemplary for a key exchange mechanism (KEM) I provide [PQC Sike key exchange mechanism (KEM) using Liboqs library](pqc_sike_liboqs_kem.md) and for a signature I provide [PQC FALCON signature using Liboqs library](pqc_falcon_liboqs_signature.md).

Last update: Mar. 19th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)
