import { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { fetchCurrentEmployee } from "../../services/EmployeeService";
import { Loading } from "../loader/LoadingView";
import PasswordChangeModal from "./PasswordChangeModal";
import ProfileData from "./ProfileData";

export default function Profile() {
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);
  const [modalShow, setModalShow] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetchCurrentEmployee()
      .then((data) => setEmployee(data))
      .then(() => setLoading(false));
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="container">
      <h1 className="my-3 mb-5">Profil</h1>
      {employee ? (
        <div>
          <ProfileData
            title="Imię i nazwisko"
            value={employee.firstName + " " + employee.lastName}
          />
          <ProfileData title="Adres e-email" value={employee.email} />
          <ProfileData title="Numer telefonu" value={employee.phone} />
          <ProfileData title="Adres" value={employee.address} />
          <div className="mt-4">
            <Button onClick={() => setModalShow(true)} variant="primary">
              Zmień hasło
            </Button>
          </div>
          <PasswordChangeModal
            show={modalShow}
            onHide={() => setModalShow(false)}
          />
        </div>
      ) : null}
    </div>
  );
}
