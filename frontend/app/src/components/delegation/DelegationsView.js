import {Button, Table} from "react-bootstrap";
import { Link } from "react-router-dom";
import { fetchUserDelegationRequests } from "../../services/DocumentsService";
import SingleDelegationDocument from "./SingleDelegationDocument";
import { useEffect, useState } from "react";

export default function DelegationsView() {
  
  const [delegations, setDelegations] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetchUserDelegationRequests()
      .then((data) => setDelegations(data))
      .then(setLoading(false));
  }, []);
  
  return (
    <div className="container">
      <h1 className="my-3">Delegacje</h1>
      <Link to="/delegations/add">
        <Button variant="primary">Złóż wniosek o delegację</Button>
      </Link>
      <div>
        <Table bordered hover size="sm" className="my-5 col-sm-8">
            <thead>
              <tr>
                <th>Data rozpoczęcia</th>
                <th>Data zakończenia</th>
                <th>Miejsce delegacji</th>
                <th>Status</th>
              </tr>
            </thead>

            {loading ? (
              <tbody></tbody>
            ) : (
              <tbody>
                {delegations.length &&
                  delegations.map((delegation) => (
                    <SingleDelegationDocument key={delegation.id} delegation={delegation} />
                ))}
              </tbody>
            )}
          </Table>
        </div>
    </div>
    
  );
}
