import socket
import threading
import sys


def handle_client(conn, addr):
    print(f"Connected by {addr}", flush=True)
    with conn:
        while True:
            try:
                data = conn.recv(1024)
                if not data:
                    break
                conn.sendall(data)
            except (ConnectionResetError, ConnectionAbortedError, socket.error) as e:
                print(f"Client {addr} disconnected with error: {e}", flush=True)
                break


def listen_on_address(family, host, port):
    try:
        s = socket.socket(family, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        if hasattr(socket, "SO_EXCLUSIVEADDRUSE") and sys.platform == "win32":
            s.setsockopt(socket.SOL_SOCKET, socket.SO_EXCLUSIVEADDRUSE, 0)

        s.bind((host, port))
        s.listen(128)
        print(f"Successfully listening on {host}:{port}", flush=True)

        while True:
            try:
                conn, addr = s.accept()
                threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
            except Exception as e:
                print(f"Accept error on {host}:{port}: {e}", file=sys.stderr, flush=True)
                break
    except Exception as e:
        print(f"Could not bind to {host}:{port}: {e}", file=sys.stderr, flush=True)


def start_server(port=16778):
    t4 = threading.Thread(target=listen_on_address, args=(socket.AF_INET, "127.0.0.1", port), daemon=True)
    t6 = threading.Thread(target=listen_on_address, args=(socket.AF_INET6, "::1", port), daemon=True)
    t4.start()
    t6.start()
    print(f"Server started on port {port}.", flush=True)
    t4.join()
    t6.join()


if __name__ == '__main__':
    start_server()