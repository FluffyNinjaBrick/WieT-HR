import CalendarComponent from "../calendar/CalendarComponent";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/Button";

export default function LeavesView() {
  return(
    <div className="container justify-content-sm-center">
      <div><h1>Pracownicy/Urlopy</h1></div>
      <CalendarComponent />
      <div>
        <Link to ="/leaves/add">
          <Button variant="primary">
            Złóż wniosek o urlop
          </Button>
        </Link>
      </div>
    </div>



  );
}
 