import { useEffect, useState } from "react";
import { API_URL } from "../../api/Api";
import { Chart } from "react-google-charts";
import { Table } from "react-bootstrap";
import { getCurrentUser } from "../../services/AuthService";
import { LoadingComponent } from "../loader/LoadingView";
import { Form } from "react-bootstrap";
import { DatePickerContainer } from "../../styled";

//TODO mozliwosc wyboru roku
export default function CalendarView() {
  const token = JSON.parse(getCurrentUser()).jwt;
  const auth = "Bearer " + token;

  const [absentEmployeesData, setAbsentEmployeesData] = useState([]);
  const [currentEmployeesShown, setcurrentEmployeesShown] = useState([]);
  const [viewedYear, setViewedYear] = useState(new Date().getFullYear());

  var currentViewedDay = null;
  const fetchAbsentEmployeesInfo = async () => {
    const absentEmpPerDay = await fetch(
      API_URL + `employees/absent/${viewedYear}-01-01/${viewedYear}-12-31`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: auth,
        },
      }
    );
    let resultJson = await absentEmpPerDay.json();
    console.log(resultJson);
    return resultJson;
  };

  useEffect(() => {
    fetchAbsentEmployeesInfo().then((data) => setAbsentEmployeesData(data));
    setcurrentEmployeesShown();
  }, [viewedYear]);

  function convertAbsentEmployeesInfo() {
    //converted for chart drawing purpose. Structure: [Date, Number of absent employees]
    var convertedToCorrectStructure = [];
    var splitted, year, month, day;
    for (var x = 0; x < absentEmployeesData.length; x++) {
      splitted = absentEmployeesData[x]["day"].split("-");
      year = splitted[0];
      month = splitted[1] - 1;
      day = splitted[2];
      convertedToCorrectStructure.push([
        new Date(year, month, day),
        absentEmployeesData[x]["absentEmployees"].length,
      ]);
    }
    return convertedToCorrectStructure;
  }

  function generateEmployeeTable(employeeList, date) {
    var id = -1;
    if (employeeList.length === 0) {
      return (
        <div>
          <h4>W dniu {date} wszyscy pracownicy są dostępni</h4>
        </div>
      );
    } else {
      return (
        <div className="mt-2">
          <h4>Pracownicy niedostępni w dniu {date}</h4>
          <div className="col-md-4 p-0">
            <Table bordered hover striped size="md">
              <thead>
                <tr>
                  <th>Imię</th>
                  <th>Nazwisko</th>
                </tr>
              </thead>
              <tbody>
                {employeeList.map((employee) => {
                  id++;
                  return (
                    <tr key={id}>
                      <td>{employee.split(" ")[0]}</td>
                      <td>{employee.split(" ")[1]}</td>
                    </tr>
                  );
                })}
              </tbody>
            </Table>
          </div>
        </div>
      );
    }
  }

  var chartData = convertAbsentEmployeesInfo();
  //map of absent employees on give day. key: Date (yyyy-MM-DD) value:list of absent employees
  var absentEmployeesMap = new Map();
  //converting employee list to map
  if (absentEmployeesData.length !== 0) {
    absentEmployeesData.map((v) =>
      absentEmployeesMap.set(v["day"], v["absentEmployees"])
    );
  }

  function handleYearChange(e) {
    setViewedYear(e.target.value);
  }

  const data = [
    [
      {
        type: "date",
        id: "Date",
      },
      {
        type: "number",
        id: "numberOfEmployeesAbsent",
      },
    ],
  ];
  //data with necessary column definitions for creating the chart
  var dataWithTypes = data.concat(chartData);

  if (absentEmployeesData.length === 0) {
    return <LoadingComponent />;
  } else {
    return (
      <div className="mt-5">
        <DatePickerContainer className="my-3">
          <h4 className="m-0 mr-2">Rok: </h4>
          <div>
            <Form.Control
              onChange={handleYearChange}
              as="select"
              defaultValue={new Date().getFullYear()}
            >
              <option>{new Date().getFullYear() + 1}</option>
              <option>{new Date().getFullYear()}</option>
              <option>{new Date().getFullYear() - 1}</option>
            </Form.Control>
          </div>
        </DatePickerContainer>
        <Chart
          width={1000}
          height={200}
          chartType="Calendar"
          loader={<div>Loading Chart</div>}
          data={dataWithTypes}
          options={{
            title: "Liczba pracowników niedostępnych w danym dniu",
            noDataPattern: {
              backgroundColor: "#add8e6",
              color: "#FFFFFF",
            },
            colorAxis: { minValue: 0, colors: ["#e3f1ff", "#00008b"] },
            calendar: {
              cellColor: {
                stroke: "#CCCCCC",
                strokeOpacity: 0.5,
                strokeWidth: 1,
              },
              focusedCellColor: {
                stroke: "#666600",
                strokeOpacity: 1,
                strokeWidth: 1,
              },
              monthOutlineColor: {
                stroke: "#A1433D",
                strokeOpacity: 0.3,
                strokeWidth: 1,
              },
            },
          }}
          chartEvents={[
            {
              eventName: "ready",
              callback: ({ chartWrapper, google }) => {
                const chart = chartWrapper.getChart();
                google.visualization.events.addListener(
                  chart,
                  "select",
                  (e) => {
                    //converting to date with format of YYYY-MM-DD. Alternatively to slice, split("T")[0] would work
                    var selection = chart.getSelection()[0];
                    try {
                      if (selection !== undefined && "row" in selection) {
                        var checkedDay = new Date(selection["date"])
                          .toISOString()
                          .slice(0, 10);
                        if (currentViewedDay === checkedDay) {
                          currentViewedDay = null;
                          setcurrentEmployeesShown();
                        } else {
                          var employeesList =
                            absentEmployeesMap.get(checkedDay);
                          currentViewedDay = checkedDay;
                          setcurrentEmployeesShown(
                            generateEmployeeTable(employeesList, checkedDay)
                          );
                        }
                      }
                    } catch (error) {}
                  }
                );
              },
            },
          ]}
        />
        {currentEmployeesShown}
      </div>
    );
  }
}
