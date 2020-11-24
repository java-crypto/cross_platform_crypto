module.exports = function (buf) {
    if (!buf) return undefined;
    if (buf.constructor.name === 'Uint8Array'
    || buf.constructor === Uint8Array) {
        return buf;
    }
    if (typeof buf === 'string') buf = Buffer(buf);
    var a = new Uint8Array(buf.length);
    for (var i = 0; i < buf.length; i++) a[i] = buf[i];
    return a;
};
