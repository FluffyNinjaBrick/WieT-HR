import AddBonusForm from "./AddContractForm";
import { useLocation } from "react-router";
import AddContractForm from "./AddContractForm";

export default function AddContractView() {
  
    let location = useLocation();
    const employeeToAddContract = (location.state && location.state.employee) || {};
  
    return (
    <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
        <h1 className="mt-5">Dodaj nowy kontrakt dla {employeeToAddContract.firstName} {employeeToAddContract.lastName}</h1>
        <AddContractForm
                employee={employeeToAddContract}
         />
    </div>
    );
}