package main

import (
  "crypto/md5"
  "encoding/hex"
	"fmt"
  "strconv"
)

func main() {

  fmt.Println("Generate a MD5 hash")

  fmt.Println("\n# # # SECURITY WARNING: This code is provided for achieve    # # #")
  fmt.Println("# # # compatibility between different programming languages. # # #")
  fmt.Println("# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #")
  fmt.Println("# # # The hash algorithm MD5 is BROKEN.                      # # #")
  fmt.Println("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n")  

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Println("plaintext:                " + plaintext)

  md5Value := CalculateMd5(plaintext)
  md5Len := len(md5Value)
  md5String := hex.EncodeToString(md5Value)  
  fmt.Println("md5Value (hex) length: " + strconv.Itoa(md5Len) + " data: " + md5String)
}

func CalculateMd5(string string)([]byte){
  dataToHash := []byte(string)
  msgHash := md5.New()
  _, err := msgHash.Write(dataToHash)
  if err != nil {
	  panic(err)
  }
  msgHashSum := msgHash.Sum(nil)
  return msgHashSum
}
