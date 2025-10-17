from http import HTTPStatus
import http
import os
import http.server
import socketserver

class MyServer(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):

        print(self.path)
        
        if self.path == '/':
            #self.path = '/home/furas/tests/index.html'
            self.path = './desktop/formdailyactivities/index.html'
            
            print('original  :', self.path)
            print('translated:', self.translate_path(self.path))
            
            try:
                f = open(self.path, 'rb')
            except OSError:
                self.send_error(HTTPStatus.NOT_FOUND, "File not found")
                return None

            ctype = self.guess_type(self.path)
            fs = os.fstat(f.fileno())
            
            self.send_response(200)
            self.send_header("Content-type", ctype)
            self.send_header("Content-Length", str(fs[6]))
            self.send_header("Last-Modified", self.date_time_string(fs.st_mtime))
            self.end_headers()            
            
            try:
                self.copyfile(f, self.wfile)
            finally:
                f.close()
                
        else:
            # run normal code
            print('original  :', self.path)
            print('translated:', self.translate_path(self.path))
            super().do_GET()