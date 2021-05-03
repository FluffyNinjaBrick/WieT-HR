import { Button, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { fetchUserDelegationRequests } from "../../services/DocumentsService";
import SingleDelegationDocument from "./SingleDelegationDocument";
import { useEffect, useState } from "react";
import { Loading } from "../loader/LoadingView";

export default function DelegationsView() {
  const [delegations, setDelegations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);

    fetchUserDelegationRequests()
      .then((data) => setDelegations(data))
      .then(() => setLoading(false));
  }, []);

  if(loading) {
    return <Loading />;
  }

  return (
    <div className="container">
      <h1 className="my-3">Delegacje</h1>
      <Link to="/delegations/add">
        <Button variant="primary">Złóż wniosek o delegację</Button>
      </Link>
      <div>
        <h3 className="mt-5">Twoje wnioski o delegację</h3>
      </div>
      <div>
        {loading ? (
          <Loading />
        ) : (
          <div>
            {delegations.length && !loading ? (
              <Table bordered hover size="sm" className="my-3">
                <thead>
                  <tr>
                    <th>Data złożenia wniosku</th>
                    <th>Data rozpoczęcia</th>
                    <th>Data zakończenia</th>
                    <th>Miejsce delegacji</th>
                    <th>Status</th>
                    <th>Pliki</th>
                  </tr>
                </thead>

                <tbody>
                  {delegations.map((delegation) => (
                    <SingleDelegationDocument
                      key={delegation.id}
                      delegation={delegation}
                    />
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
