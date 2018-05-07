package com.lj.app.controllers;

import java.util.List;

import javax.validation.Valid;

import com.lj.app.models.Member;
import com.lj.app.repositories.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class MemberController {


    @Autowired
    MemberRepository memberRepo;

    @GetMapping("/members")
    public List<Member> getAll(){
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        return memberRepo.findAll(sortByCreatedAtDesc);
    }

    @PostMapping("/members")
    public Member add( @Valid @RequestBody Member member){
        return memberRepo.save(member);
    }

    @DeleteMapping(value="/members/{id}")
    public void delete(@PathVariable("id") String id) {
        memberRepo.delete(id);
    }

}