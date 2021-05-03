import EmployeeEditForm from "./EmployeeEditForm";
import { useHistory, useParams } from "react-router";
import { Button } from "react-bootstrap";
import { API_URL } from "../../api/Api";
import { getCurrentUser } from "../../services/AuthService";

export default function EmployeeEditView() {
  const { id } = useParams();
  const history = useHistory();

  const deleteEmployee = async () => {
    const user = JSON.parse(getCurrentUser());
    const token = user ? user.jwt : "";
    const auth = "Bearer " + token;
    const response = await fetch(API_URL + "employees/" + id, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: auth,
      },
    });
    if (response.ok) {
      history.push("/employees");
    }
  };

  return (
    <div className="container">
      <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
        <h1 className="mt-5">Edytuj pracownika</h1>
        <Button
          onClick={() => deleteEmployee()}
          variant="danger"
          className="mt-2"
        >
          Usuń pracownika
        </Button>
        <hr />
      </div>
      <EmployeeEditForm employeeId={id} />
    </div>
  );
}
