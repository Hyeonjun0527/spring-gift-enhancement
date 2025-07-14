package gift.controller;

import gift.dto.RegisterRequest;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")

public class MemeberAdminController {

    private MemberService memberService;

    public MemeberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String showMemberList(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "member/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "member/create-form";
    }

    @PostMapping("/new")
    public String addMember(@Valid @ModelAttribute RegisterRequest request,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "member/create-form";
        }

        memberService.save(request);
        return "redirect:/admin/members";
    }

}
