import CalendarView from "../calendar/CalendarView";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/Button";

export default function LeavesViewAdmin() {
  return (
    <div className="container justify-content-sm-center">
      <div>
        <h1 className="my-3">Pracownicy / urlopy</h1>
      </div>
      <CalendarView />
    </div>
  );
}
