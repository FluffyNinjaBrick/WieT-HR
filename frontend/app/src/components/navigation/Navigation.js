import { Navbar, Nav, NavDropdown } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useAuth } from "../auth/useAuth";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Navigation() {
  const auth = useAuth();

  if (!auth.user) {
    return <div></div>;
  }

  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand as={Link} to="/profile">
        WieT-HR
      </Navbar.Brand>

      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link as={Link} to="/leaves">
            Urlopy
          </Nav.Link>
          <Nav.Link as={Link} to="/delegations">
            Delegacje
          </Nav.Link>
          <Nav.Link as={Link} to="/bonuses">
            Premie
          </Nav.Link>
          <Nav.Link as={Link} to="/contract">
            Umowa
          </Nav.Link>
          {auth.user && (auth.user.userRole === "ADMIN" || auth.user.userRole === "MANAGER") && (
            <NavDropdown title="Pracownicy" id="basic-nav-dropdown">
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees">
                  Dane
                </Nav.Link>
              </NavDropdown.Item>
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees/requests">
                  Wnioski
                </Nav.Link>
              </NavDropdown.Item>
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees/leaves">
                  Urlopy
                </Nav.Link>
              </NavDropdown.Item>
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees/delegations">
                  Delegacje
                </Nav.Link>
              </NavDropdown.Item>
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees/bonuses">
                  Premie
                </Nav.Link>
              </NavDropdown.Item>
              <NavDropdown.Item>
                <Nav.Link as={Link} to="/employees/salaries">
                  Wynagrodzenia
                </Nav.Link>
              </NavDropdown.Item>
            </NavDropdown>
          )}
        </Nav>
        {auth.user && (
          <Nav className="justify-content-end">
            <Nav.Link as={Link} to="/profile">
              Profil
            </Nav.Link>
            <Nav.Link as={Link} onClick={() => auth.logout()}>
              Wyloguj
            </Nav.Link>
          </Nav>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
}
