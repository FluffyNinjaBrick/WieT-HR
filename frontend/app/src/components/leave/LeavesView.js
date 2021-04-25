import { Link } from "react-router-dom";
import { Button, ProgressBar } from "react-bootstrap";

//TODO wziac info o pracowniku z api i wtedy wyciagnac jego dni wolne.
//TODO przeniesc yearsDaysOff jako zmienna bardziej globalna xd
export default function LeavesView() {
  var yearsDaysOff = 10;
  var daysOffLeft = 5//employee.thisYearDaysOff;
  var colorVariant;

  switch(parseInt(daysOffLeft * 3 /yearsDaysOff)){
      case 0:
          colorVariant = "danger";
          break
      case 1:
          colorVariant = "warning";
          break
      case 2:
          colorVariant = "info";
          break
      default:
          colorVariant = "info";
          break;   
  }
  
  return (
    <div className="container justify-content-sm-center">
      <div>
        <h1 className="my-3">Urlopy</h1>
      </div>
      <h6>Masz aktualnie dostępne {daysOffLeft} dni wolnych. W tym roku wykorzystałeś/aś ich juz {yearsDaysOff - daysOffLeft}.</h6>
      <div style={{width:"55%"}}>
        <ProgressBar className="my-1" now={daysOffLeft * 10} label={daysOffLeft + "/" + yearsDaysOff} variant={colorVariant}/>
      </div>
      <div>
        <Link to="/leaves/add">
          <Button variant="primary">Złóż wniosek o urlop</Button>
        </Link>
      </div>
    </div>
  );
}
