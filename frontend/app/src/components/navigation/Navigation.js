import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from "react-bootstrap";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Navigation() {
  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand>
        <Link to="/profile" style={{ textDecoration: "none" }}>
          WieT-HR
        </Link>
      </Navbar.Brand>

      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link>
            <Link to="/leaves">Urlopy</Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/profile">Profil</Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/delegations">Delegacje</Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/contract">Umowa</Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/bonuses">Premie</Link>
          </Nav.Link>
          <NavDropdown title="Pracownicy" id="basic-nav-dropdown">
            <NavDropdown.Item>
              <Link to="/employees">Dane</Link>
            </NavDropdown.Item>
            <NavDropdown.Item>
              <Link to="/employees/leaves">Urlopy</Link>
            </NavDropdown.Item>
            <NavDropdown.Item>
              <Link to="/employees/delegations">Delegacje</Link>
            </NavDropdown.Item>
            <NavDropdown.Item>
              <Link to="/employees/bonuses">Premie</Link>
            </NavDropdown.Item>
          </NavDropdown>
        </Nav>
        <Nav className="justify-content-end">
          <Nav.Link>Wyloguj</Nav.Link>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}
