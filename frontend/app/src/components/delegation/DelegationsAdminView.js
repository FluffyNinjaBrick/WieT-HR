import { useEffect, useState } from "react";
import BootstrapTable from "react-bootstrap-table-next";
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
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    fetchAllDelegations()
      .then((data) => {
        setAllDelegations(data.filter((x) => x.signed));
      })
      .then(setLoading(false));
  }, []);

  const columns = [
    {
      dataField: "name",
      text: "Imię i nazwisko",
      filter: textFilter({
        placeholder: "Podaj imie i nazwisko",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateFrom",
      text: "Data od",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.GE],
        comparatorStyle: { display: "none" },
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
        comparatorStyle: { display: "none" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "destination",
      text: "Miejsce",
      filter: textFilter({
        placeholder: "Podaj miejsce delegacji",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "status",
      text: "Status",
      filter: selectFilter({
        options: {
          Zaakceptowany: "Zaakceptowany",
          Oczekujący: "Oczekujący",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
  ];

  // if (allDelegations.length === 0) {
  //   return (
  //     <div
  //       className="mx-3 my-3 justify-content-sm-center"
  //       style={{ top: "50%", left: "50%", position: "fixed" }}
  //     >
  //       <Loading />
  //     </div>
  //   );
  return (
    <div className="container justify-content-sm-center">
      <h1 className="my-4">Pracownicy / Delegacje</h1>
      {loading ? (
        <Loading />
      ) : (
        <div>
          {allDelegations.length ? (
            <BootstrapTable
              keyField="id"
              data={allDelegations.map((delegation) => {
                let x = {
                  name:
                    delegation.employee.firstName +
                    " " +
                    delegation.employee.lastName,
                  dateFrom: delegation.dateFrom,
                  dateTo: delegation.dateTo,
                  destination: delegation.destination,
                  status: delegation.signed ? "Zaakceptowany" : "Oczekujący",
                };
                return x;
              })}
              columns={columns}
              filter={filterFactory()}
              filterPosition="top"
              striped
              hover
            />
          ) : (
            <div className="mt-3">
              <h6>Nie znaleziono żadnych delegacji.</h6>
            </div>
          )}
        </div>
      )}

      {/* <Button variant="info" size="md" className="mb-5" disabled={isLoading || areAllLoaded} 
                    onClick={!isLoading ? loadMoreCharacters : null} block> 
                        {areAllLoaded ?  "All characters loaded!" :  !isLoading? "Load more characters..." : "Loading ..."}
                </Button> */}
    </div>
  );
}
