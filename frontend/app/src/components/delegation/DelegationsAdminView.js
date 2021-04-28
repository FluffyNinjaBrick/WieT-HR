import { useEffect, useState } from "react";
import BootstrapTable from "react-bootstrap-table-next";
import { Button } from "react-bootstrap";
import filterFactory, {
  textFilter,
  selectFilter,
  dateFilter,
  Comparator,
} from "react-bootstrap-table2-filter";
import { Loading } from "../loader/LoadingView";
import { fetchAllDelegations } from "../../services/DocumentsService";

export default function DelegationsAdminView() {
  const [allDelegations, setAllDelegations] = useState([]);

  useEffect(() => {
    fetchAllDelegations().then((data) => {
      setAllDelegations(data);
      console.log(data);
    });
  }, []);

  const columns = [
    {
      dataField: "name",
      text: "Imię i nazwisko",
      filter: textFilter(),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateFrom",
      text: "Data od",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.GE],
        comparatorStyle: { position: "absolute", width: "0px", height: "0px" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateTo",
      text: "Data do",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.LE],
        comparatorStyle: { position: "absolute", width: "0px", height: "0px" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "leaveType",
      text: "Typ urlopu",
      filter: selectFilter({
        options: {
          SICK: "Chorobowy",
          MATERNITY: "Macierzyński",
          RECREATIONAL: "Rekreacyjny",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "status",
      text: "Status",
      filter: selectFilter({
        options: {
          "Zaakceptowany": "Zaakceptowany",
          "Oczekujący": "Oczekujący",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
  ];

  if (allDelegations.length === 0) {
    return (
      <div
        className="mx-3 my-3 justify-content-sm-center"
        style={{ top: "50%", left: "50%", position: "fixed" }}
      >
        <Loading />
      </div>
    );
  } else {
    return (
      <div className="col-sm-9 mx-5 my-4">
        <BootstrapTable
          keyField="name"
          data={allDelegations.map((delegation) => {
            let x = {'name': delegation.employee.firstName + " " + delegation.employee.lastName, 'dateFrom': delegation.dateFrom, 'dateTo': delegation.dateTo, 'leaveType': "prosze to naprawic", 'status': (delegation.signed ? "Zaakceptowany" : "Oczekujący")};

            console.log(x);
            return x;
          })}
          columns={columns}
          filter={filterFactory()}
          filterPosition="top"
          striped
          hover
        />

        {/* <Button variant="info" size="md" className="mb-5" disabled={isLoading || areAllLoaded} 
                    onClick={!isLoading ? loadMoreCharacters : null} block> 
                        {areAllLoaded ?  "All characters loaded!" :  !isLoading? "Load more characters..." : "Loading ..."}
                </Button> */}
      </div>
    );
  }
}
