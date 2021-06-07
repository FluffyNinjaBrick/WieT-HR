import AddBonusForm from "./AddContractForm";
import { useLocation, useParams } from "react-router";
import AddContractForm from "./AddContractForm";
import { useState, useEffect } from "react";
import { useQuery } from "react-query";
import { fetchEmployee } from "../../services/EmployeeService";
import { Loading } from "../loader/LoadingView";

export default function AddContractView() {
  const { id } = useParams();
  const [employee, setEmployee] = useState(null);

  const {
    isLoading,
    error,
    data: apiResponse,
    refetch,
    isFetching,
    status,
  } = useQuery("employeeContract", () => fetchEmployee(id));

  useEffect(() => {
    setEmployee(apiResponse?.data);
  }, [apiResponse]);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
      {!employee || isLoading ? (
        <Loading />
      ) : (
        <>
          <h1 className="mt-5">
            Dodaj nowy kontrakt dla {employee.firstName} {employee.lastName}
          </h1>
          <AddContractForm employee={employee} />
        </>
      )}
    </div>
  );
}
