import { Link } from "react-router-dom";
import Button from "react-bootstrap/Button";

export default function LeavesView() {
  return (
    <div className="container justify-content-sm-center">
      <div>
        <h1 className="my-3">Urlopy</h1>
      </div>
      <div>
        <Link to="/leaves/add">
          <Button variant="primary">Złóż wniosek o urlop</Button>
        </Link>
      </div>
    </div>
  );
}
