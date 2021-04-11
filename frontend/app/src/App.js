import "./App.css";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
  Link,
} from "react-router-dom";
import Navigation from "./components/navigation/Navigation";
import LeavesView from "./components/leave/LeavesView";
import DelegationsView from "./components/delegation/DelegationsView";
import Profile from "./components/profile/Profile";
import ContractView from "./components/contract/ContractView";
import BonusesView from "./components/bonus/BonusesView";
import EmployeeListViewAdmin from "./components/employee/EmployeeListViewAdmin";
import EmployeeEditView from "./components/employee/EmployeeEditView";
import LoginView from "./components/auth/LoginView";
import EmployeeCreateView from "./components/employee/EmployeeCreateView";

function App() {
  return (
    <Router>
      <div>
        <Navigation />

        <Switch>
          <Route exact path="/">
            <Redirect to="/profile" />
          </Route>
          <Route path="/profile">
            <Profile />
          </Route>
          <Route path="/delegations">
            <DelegationsView />
          </Route>
          <Route exact path="/employees">
            <EmployeeListViewAdmin />
          </Route>
          <Route path="/employees/edit/:id">
            <EmployeeEditView />
          </Route>
          <Route path="/employees/create">
            <EmployeeCreateView />
          </Route>
          {/* tutaj trzeba bedzie zrobic routy dla urlopu pracownika i urlopow wszystkich 
          pracownikow, jak sie maprawa do ich wyswietlania, dropdown pracownicy -> urlopy */}
          <Route path="/employees/leaves">
            <LeavesView />
          </Route>
          <Route path="/leaves">
            <LeavesView />
          </Route>
          <Route path="/contract">
            <ContractView />
          </Route>
          <Route path="/bonuses">
            <BonusesView />
          </Route>
          <Route path="/login">
            <LoginView />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
