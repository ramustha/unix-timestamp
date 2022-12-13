java -jar publish/marketplace-zip-signer-cli.jar sign\
  -in "build/distributions/unix-timestamp-4.0.0.zip"\
  -out "build/distributions/unix-timestamp-4.0.0-signed.zip"\
  -cert-file "publish/chain.crt"\
  -key-file "publish/private.pem"\
  -key-pass {{password}}
