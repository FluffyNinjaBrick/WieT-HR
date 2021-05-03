import { Link } from "react-router-dom";
import { Button, ProgressBar, Table } from "react-bootstrap";
import {
  fetchUserDaysOff,
  fetchUserDaysOffSummary,
} from "../../services/DocumentsService";
import { useEffect, useState } from "react";
import SingleLeaveDocument from "./SingleLeaveDocument";
import { Loading } from "../loader/LoadingView";

export default function LeavesView() {
  var colorVariant;

  const [daysOffRequests, setDaysOffRequests] = useState([]);
  const [daysOffTotal, setDaysOffTotal] = useState(0);
  const [daysOffLeft, setDaysOffLeft] = useState(0);
  const [loading, setLoading] = useState(true);

  switch (parseInt((daysOffLeft * 4) / daysOffTotal)) {
    case 0:
      colorVariant = "danger";
      break;
    case 1:
      colorVariant = "warning";
      break;
    case 2:
      colorVariant = "info";
      break;
    default:
      colorVariant = "success";
      break;
  }

  useEffect(() => {
    setLoading(true);

    fetchUserDaysOffSummary().then((data) => {
      if (data) {
        setDaysOffLeft(data.daysOffLeft);
        setDaysOffTotal(data.daysOffLeft + data.daysOffUsed);
      }
    });

    fetchUserDaysOff()
      .then((data) => {
        setDaysOffRequests(data);
        // if (data) {
        //   setDaysOffLeft(
        //     data[0].employee.thisYearDaysOff + data[0].employee.lastYearDaysOff
        //   );
        //   setDaysOffTotal(30);
        // }
      })
      .then(() => setLoading(false));
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="container justify-content-sm-center">
      <div>
        <h1 className="mt-3">Urlopy</h1>
      </div>
      <div className="my-3 mb-5">
        <Link to="/leaves/add">
          <Button variant="primary">Złóż wniosek o urlop</Button>
        </Link>
      </div>
      <div>
        <h3 className="mt-5">Twoje dni wolne</h3>
      </div>
      {loading ? (
        <Loading />
      ) : (
        <div className="my-3">
          <h6>
            Masz aktualnie dostępne {daysOffLeft} dni wolnych. W tym roku
            wykorzystałeś/aś ich już {daysOffTotal - daysOffLeft}.
          </h6>
          <ProgressBar
            className="my-1"
            now={(daysOffLeft * 100) / daysOffTotal}
            label={daysOffLeft + "/" + daysOffTotal}
            variant={colorVariant}
            style={{ height: "30px" }}
          />
        </div>
      )}
      <div>
        <h3 className="mt-5">Twoje wnioski urlopowe</h3>
      </div>
      <div>
        {loading ? (
          <Loading />
        ) : (
          <div>
            {daysOffRequests.length && !loading ? (
              <Table bordered hover size="sm" className="my-3">
                <thead>
                  <tr>
                    <th>Data złożenia wniosku</th>
                    <th>Data rozpoczęcia</th>
                    <th>Data zakończenia</th>
                    <th>Typ urlopu</th>
                    <th>Status</th>
                    <th>Pliki</th>
                  </tr>
                </thead>
                <tbody>
                  {daysOffRequests.map((leave) => (
                    <SingleLeaveDocument key={leave.id} leave={leave} />
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="mt-3">
                <h6>Nie znaleziono żadnych wniosków.</h6>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
