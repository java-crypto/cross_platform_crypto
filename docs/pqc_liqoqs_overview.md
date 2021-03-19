# Cross-platform cryptography

## Post Quantum Cryptography (PQC) with library Liboqs overview

For a general overview about Post Quantum Computing I recommend to read my article [PQC overview page](pqc_overview.md). This page is about a specialized library that computes nearly all algorithms that are part of round 3 of a competition to find the new standard algorithm(s) for a secure key exchange and signature.

More details about the competition can be found here: [NIST Post-Quantum Cryptography PQC Round 3  Submissions](https://csrc.nist.gov/projects/post-quantum-cryptography/round-3-submissions).

For my PQC-project I tried to find libraries that run the competition algorithms but it is very hard to find them (no matter in which framework I searched). The only library that runs nearly all algorithms is the one from the [**Open Quantum Safe (OQS) project**](https://openquantumsafe.org/), the source code is available on GitHub: [https://github.com/open-quantum-safe](https://github.com/open-quantum-safe).

The core library is written in "C" and there are bindings available ("language wrapper") for the frameworks C++, Go, Java, .Net, Python and Rust. Sounds good to me but there is on point that I can't solve on my own: you need to compile the library on your own and **there are no downloadable compiled versions available**.

With my limited resources and lack of knowledge I'm not been able to master this hurdle. But with the kindly help of the OpenQuantumSafe-team I got the information that there is a "ready to use" docker file available that can be used with Java to build the library and run the tests provided in the Java wrapper [liboqs-java](https://github.com/open-quantum-safe/liboqs-java).

To make it short: I installed docker in a vmWare image of Ubuntu, followed the instructions to download and install the core lib, compiled it and run the algorithm tests from liboqs-java successfully. The next step was to clone the original GitHub repository of liboqs-java and changed the tests that I got a lot of details and I'm providing them in the following articles.





### Why don't you use the Open Quantum Safe (OQS) library?

If you are looking for a cross-platform library that is capable of all round 3 candidates then I'm recommending to visit the [Open Quantum Safe (OQS) project](https://openquantumsafe.org/) and the GitHub repository [https://github.com/open-quantum-safe](https://github.com/open-quantum-safe). The libraries could be perfect for my Cross platform cryptography project but unfortunately they do not provide any libraries in compiled form. My limited resources do not allow me to investigate in the compiling and binding technology so I have to leave out this great opportunity.

Last update: Mar. 15th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)
