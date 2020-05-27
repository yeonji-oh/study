const http = require('http');

const hostname = '127.0.0.1';
const port = 3000;

const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('Hello World\n');

  var accountStr = '{"name":"Jedi", "members":["Yoda", "Obi Wan"], "number":34512, "location":"A galaxy far, far away"}';
  var accountObj = JSON.parse(accountStr);
  var aaa = JSON.stringify(accountObj, null, 2);

  console.log(aaa);       //Jedi
  //console.log("eee" + accountObj.members);
});

server.listen(port, hostname, () => {
  console.log('server running at http://${hostname}:${port}');
});
