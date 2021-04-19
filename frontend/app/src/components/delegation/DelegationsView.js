import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";

export default function DelegationsView() {
  return (
    <div className="container">
      <h1 className="my-3">Delegacje</h1>
      <Link to="/delegations/add">
        <Button variant="primary">Złóż wniosek o delegację</Button>
      </Link>
    </div>
  );
}
