package org.o7planning.thymeleaf.controller;

import java.util.ArrayList;
import java.util.List;

import org.o7planning.thymeleaf.form.PersonForm;
import org.o7planning.thymeleaf.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

	private static List<Person> persons = new ArrayList<Person>();

	static {
		persons.add(new Person("Bill", "Gates"));
		persons.add(new Person("Steve", "Jobs"));
	}

	// Injectez (inject) via application.properties.
	@Value("${welcome.message}")
	private String message;

	@Value("${error.message}")
	private String errorMessage;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model, @ModelAttribute("personneCree") Person personneCree) {
		System.out.println("personneCree = " + personneCree);
		model.addAttribute("message", message);

		return "index";
	}

	@RequestMapping(value = { "/personList" }, method = RequestMethod.GET)
	public String personList(Model model) {

		return listerPersonnes(null, model);
	}

	@RequestMapping(value = { "/personList/{origine}" }, method = RequestMethod.GET)
	public String personList(@PathVariable String origine, Model model, @RequestParam String att1, @ModelAttribute("flashAtt1") String flashAtt1, @ModelAttribute("personneCree") Person personneCree) {
		System.out.println("att1 = " + att1);
		System.out.println("flashAtt1 = " + flashAtt1);
		System.out.println("personneCree = " + personneCree);
		return listerPersonnes(origine, model);

	}

	private String listerPersonnes(String origine, Model model) {
		model.addAttribute("persons", persons);
		if (origine != null && origine.equals("savePerson")) {
			model.addAttribute("messageTraitementOK", "La personne a été ajouté avec succès");
		}

		return "personList";
	}

	@RequestMapping(value = { "/addPerson" }, method = RequestMethod.GET)
	public String showAddPersonPage(Model model) {

		PersonForm personForm = new PersonForm();
		model.addAttribute("personForm", personForm);

		return "addPerson";
	}

	@RequestMapping(value = { "/addPerson" }, method = RequestMethod.POST)
	public String savePerson(Model model, //
			@ModelAttribute("personForm") PersonForm personForm, RedirectAttributes redirectAttributes) {

		String firstName = personForm.getFirstName();
		String lastName = personForm.getLastName();

		if (firstName != null && firstName.length() > 0 //
				&& lastName != null && lastName.length() > 0) {
			Person newPerson = new Person(firstName, lastName);
			persons.add(newPerson);
			redirectAttributes.addAttribute("att1", "val1");
			redirectAttributes.addFlashAttribute("flashAtt1", "flashVal1");
			redirectAttributes.addFlashAttribute("personneCree", newPerson);

			return "redirect:/personList/savePerson";
		}

		model.addAttribute("errorMessage", errorMessage);
		return "addPerson";
	}
}
