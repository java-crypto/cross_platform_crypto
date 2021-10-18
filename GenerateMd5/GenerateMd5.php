<?php

function calculateMd5($input) {
    return hash('md5', $input, true);
}

function bytesToHex($input) {
    return bin2hex($input);
}

echo 'Generate a MD5 hash' . PHP_EOL;

echo PHP_EOL . '# # # SECURITY WARNING: This code is provided for achieve    # # #' . PHP_EOL;
echo '# # # compatibility between different programming languages. # # #' . PHP_EOL;
echo '# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #' . PHP_EOL;
echo '# # # The hash algorithm MD5 is BROKEN.                      # # #' . PHP_EOL;
echo '# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #' . PHP_EOL . PHP_EOL;

$plaintext = "The quick brown fox jumps over the lazy dog";
echo 'plaintext:                ' . $plaintext . PHP_EOL;

$md5Value = calculateMd5($plaintext);
echo 'md5Value (hex) length: ' . strlen($md5Value) . " data: " . bytesToHex($md5Value) . PHP_EOL;
?>
