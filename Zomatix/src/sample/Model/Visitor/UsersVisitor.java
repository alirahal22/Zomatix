package sample.Model.Visitor;

import sample.Model.Objects.Users.Customer;
import sample.Model.Objects.Users.Employee;
import sample.Model.Objects.Users.Manager;

public interface UsersVisitor {
    void visit(Employee employee);
    void visit(Manager manager);
    void visit(Customer customer);
}
