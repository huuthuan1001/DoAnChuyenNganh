package FigureShop.controller;

import FigureShop.model.Role;
import FigureShop.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleRepository roleRepository;

    @GetMapping("/list")
    public String listRoles(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "admin/role-list";
    }

    @GetMapping("/add")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/role-add";
    }

    @PostMapping("/add")
    public String addRole(@Valid @ModelAttribute("role") Role role,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/role-add";
        }
        roleRepository.save(role);
        return "redirect:/admin/roles/list";
    }

    @GetMapping("/edit/{id}")
    public String editRoleForm(@PathVariable Long id, Model model) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isEmpty()) {
            // Handle not found case
            return "redirect:/admin/roles";
        }
        model.addAttribute("role", roleOptional.get());
        return "admin/role-edit";
    }

    @PostMapping("/edit/{id}")
    public String editRole(@PathVariable Long id,
                           @Valid @ModelAttribute("role") Role role,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/role-edit";
        }
        role.setId(id); // Ensure the correct ID is set
        roleRepository.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            if ("ADMIN".equals(role.getName())) {
                return "redirect:/admin/roles/list";
            }
            roleRepository.deleteById(id);
        }
        return "redirect:/admin/roles/list";
    }
}
