import { useEffect, useState } from "react";
import { fetchCurrentEmployee } from "../../services/EmployeeService";
import { Loading } from "../loader/LoadingView";
import ProfileData from "./ProfileData";

export default function Profile() {
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);

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
        </div>
      ) : null}
    </div>
  );
}
