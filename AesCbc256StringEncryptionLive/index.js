import React, { Component } from 'react';
import { render } from 'react-dom';
import './style.css';
import * as CryptoJS from 'crypto-js';

class App extends Component {
  constructor() {
    super(); 
    this.state = {
      inputText: '',
      inputKey: '',
      encryptedBase64Input: '',
      encryptedBase64: '',
      decryptKey: '',
      decryptedText: ''
    };
  }

  /*
  * Encrypt a derived hd private key with a given pin and return it in Base64 form
  */

  encryptAESOwn = (text, password) => {
    var PBKDF2_ITERATIONS = 15000;
    //generateSalt32Byte() {
    var salt = CryptoJS.lib.WordArray.random(32);
    var key = CryptoJS.PBKDF2(password, salt, {
      hasher:CryptoJS.algo.SHA256,
      keySize: 256 / 32,
      iterations: PBKDF2_ITERATIONS
    });
    // generateRandomInitvector();
    var iv = CryptoJS.lib.WordArray.random(16);
    const cipher = CryptoJS.AES.encrypt(text, key,
    {
      iv: iv,
      padding: CryptoJS.pad.Pkcs7,
      mode: CryptoJS.mode.CBC
    })
    return CryptoJS.enc.Base64.stringify(salt) + ':' + CryptoJS.enc.Base64.stringify(iv) + ':' + cipher.toString();
    //return CryptoJS.AES.encrypt(text, key).toString();
  };

  /**
   * Decrypt an encrypted message
   * @param encryptedBase64 encrypted data in base64 format
   * @param key The secret key
   * @return The decrypted content
   */
  decryptAESOwn = (data, password) => {
    var PBKDF2_ITERATIONS = 15000;
    var dataSplit = data.split(":");
    var salt = CryptoJS.enc.Base64.parse(dataSplit[0]);
    var key = CryptoJS.PBKDF2(password, salt, {
      hasher:CryptoJS.algo.SHA256,
      keySize: 256 / 32,
      iterations: PBKDF2_ITERATIONS
    });
    var iv = CryptoJS.enc.Base64.parse(dataSplit[1]);
    var ciphertextDecryption = dataSplit[2];
    const cipherDecryption = CryptoJS.AES.decrypt(ciphertextDecryption, key,
    {
      iv: iv,
      padding: CryptoJS.pad.Pkcs7,
      mode: CryptoJS.mode.CBC
    });
    return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
  };

  handleInputTextChange = (event) => {
    this.setState({
      inputText: event.target.value
    }, this.encryptInputText)
  }

  handleInputKeyChange = (event) => {
    this.setState({
      inputKey: event.target.value
    }, this.encryptInputText)
  }

  encryptInputText = () => {
    this.setState({
      encryptedBase64Input: this.encryptAESOwn(this.state.inputText, this.state.inputKey)
    })
  }

  handleDecryptKeyChange = (event) => {
    this.setState({
      decryptKey: event.target.value
    }, this.decryptOutputText)
  }

  handleMsgChange = (event) => {
    this.setState({
      encryptedBase64: event.target.value
    }, this.decryptOutputText)
  }

  decryptOutputText = () => {
    this.setState({
      outputText: this.decryptAESOwn(this.state.encryptedBase64, this.state.decryptKey)
    })
  }

  render() {
    const {error, transactions, isLoading} = this.state;
    if(error){
      return <h3>{error}</h3>
    }
    return (
      <>
        <h1>Crypto-JS AES CBC 256 PBKDF2 string encryption</h1>
        <div className="form-group">
          <input className="form-control" value={this.state.inputText} onChange={this.handleInputTextChange} style={{width:'40%', height:40, marginRight: 20}} placeholder="Input Text" /> 
          <input className="form-control" value={this.state.inputKey} onChange={this.handleInputKeyChange} style={{width:'40%', height:40}} placeholder="Key" />
        </div>
        <pre className="output"><code>{this.state.encryptedBase64Input}</code></pre>
        <h1>Crypto-JS AES CBC 256 PBKDF2 string decryption</h1>
        <div className="form-group">
          <input className="form-control" value={this.state.encryptedBase64} onChange={this.handleMsgChange} style={{width:'40%', height:40, marginRight: 20}} placeholder="Encrypted String" /> 
          <input className="form-control" value={this.state.key} onChange={this.handleDecryptKeyChange} style={{width:'40%', height:40}} placeholder="Key" />
        </div>
        <pre className="output"><code>{this.state.outputText}</code></pre>
        <small><a href="https://stackblitz.com/edit/cryptojsaescbc256pbkdf2stringencryption" target="_blank">(View source code)</a></small>
      </>
    );
  }
}

render(<App />, document.getElementById('root'));
