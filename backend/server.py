from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import re
from datetime import datetime

TOKEN_VALIDO = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.cuentasclaras_token_2026"

GROUPS = [
    {
        "id": "grp_01",
        "name": "Viaje a Guanajuato",
        "description": "Gastos de gasolina, casetas y comida del fin de semana",
        "membersCount": 4,
        "userBalance": 350.00,
        "updatedAt": "2026-11-25T12:00:00Z"
    },
    {
        "id": "grp_02",
        "name": "Gastos Depa",
        "description": "Internet, luz, agua y despensa compartida",
        "membersCount": 3,
        "userBalance": -220.50,
        "updatedAt": "2026-11-24T14:30:00Z"
    },
    {
        "id": "grp_03",
        "name": "Cena Viernes",
        "description": "Tacos y bebidas con el equipo",
        "membersCount": 5,
        "userBalance": 0.00,
        "updatedAt": "2026-11-22T22:15:00Z"
    }
]

EXPENSES = {
    "grp_01": [
        {"id": "exp_101", "groupId": "grp_01", "title": "Tanque lleno de gasolina", "amount": 950.00, "paidBy": "Isaac", "date": "2026-11-25"},
        {"id": "exp_102", "groupId": "grp_01", "title": "Casetas ida y vuelta", "amount": 250.00, "paidBy": "Carlos", "date": "2026-11-25"}
    ],
    "grp_02": [
        {"id": "exp_201", "groupId": "grp_02", "title": "Recibo de Internet Fibra", "amount": 661.50, "paidBy": "Sofía", "date": "2026-11-24"}
    ],
    "grp_03": [
        {"id": "exp_301", "groupId": "grp_03", "title": "Orden de tacos al pastor", "amount": 500.00, "paidBy": "Isaac", "date": "2026-11-22"}
    ]
}

class CuentasClarasHandler(BaseHTTPRequestHandler):
    def _send_json(self, status_code, payload):
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.end_headers()
        self.wfile.write(json.dumps(payload, ensure_ascii=False).encode("utf-8"))

    def _is_authorized(self):
        auth = self.headers.get("Authorization", "")
        return auth == f"Bearer {TOKEN_VALIDO}"

    def do_POST(self):
        content_length = int(self.headers.get("Content-Length", 0))
        body = json.loads(self.rfile.read(content_length).decode("utf-8")) if content_length > 0 else {}

        if self.path == "/api/v1/auth/login":
            email = body.get("email", "").strip()
            password = body.get("password", "").strip()
            if email and len(password) >= 4:
                self._send_json(200, {
                    "token": TOKEN_VALIDO,
                    "user": {"id": "usr_01", "name": "Isaac", "email": email}
                })
            else:
                self._send_json(401, {"error": "Credenciales inválidas. Verifica correo y contraseña."})
            return

        if not self._is_authorized():
            self._send_json(401, {"error": "Token no proporcionado o expirado"})
            return

        match = re.match(r"^/api/v1/groups/([^/]+)/expenses$", self.path)
        if match:
            group_id = match.group(1)
            title = body.get("title", "").strip()
            amount = float(body.get("amount", 0))
            paid_by = body.get("paidBy", "Isaac").strip() or "Isaac"

            if not title or amount <= 0:
                self._send_json(400, {"error": "Título y monto mayor a 0 son obligatorios"})
                return

            new_expense = {
                "id": f"exp_{int(datetime.now().timestamp())}",
                "groupId": group_id,
                "title": title,
                "amount": amount,
                "paidBy": paid_by,
                "date": datetime.now().strftime("%Y-%m-%d")
            }
            EXPENSES.setdefault(group_id, []).insert(0, new_expense)

            for g in GROUPS:
                if g["id"] == group_id:
                    share = round(amount / max(g["membersCount"], 1), 2)
                    if paid_by.lower() == "isaac":
                        g["userBalance"] = round(g["userBalance"] + (amount - share), 2)
                    else:
                        g["userBalance"] = round(g["userBalance"] - share, 2)
                    g["updatedAt"] = datetime.now().isoformat() + "Z"

            self._send_json(201, new_expense)
            return

        self._send_json(404, {"error": "Endpoint no encontrado"})

    def do_GET(self):
        if not self._is_authorized():
            self._send_json(401, {"error": "Token no proporcionado o expirado"})
            return

        if self.path == "/api/v1/groups":
            self._send_json(200, GROUPS)
            return

        match = re.match(r"^/api/v1/groups/([^/]+)/expenses$", self.path)
        if match:
            group_id = match.group(1)
            self._send_json(200, EXPENSES.get(group_id, []))
            return

        self._send_json(404, {"error": "Endpoint no encontrado"})

if __name__ == "__main__":
    server = HTTPServer(("0.0.0.0", 8080), CuentasClarasHandler)
    print("API REST CuentasClaras corriendo en http://localhost:8080/api/v1")
    server.serve_forever()